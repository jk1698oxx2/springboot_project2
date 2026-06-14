package com.example.reservation_platform.service.impl;

import com.example.reservation_platform.dto.AppointQueryResultDto;
import com.example.reservation_platform.exception.ServiceException;
import com.example.reservation_platform.mapper.Tbca003Mapper;
import com.example.reservation_platform.service.CustomerQueryService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerQueryServiceImpl implements CustomerQueryService {

    private final Tbca003Mapper tbca003Mapper;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public List<AppointQueryResultDto> getUnfinishedAppointments(String startStr, String endStr) {
        
        // --- STEP 1: 檢核欄位空值 --- [cite: 14, 19]
        if (startStr == null || startStr.trim().isEmpty() || endStr == null || endStr.trim().isEmpty()) {
            throw new ServiceException("查詢區間不得為空值，請重新輸入"); // 
        }

        LocalDateTime startTime;
        LocalDateTime endTime;

        try {
            startTime = LocalDateTime.parse(startStr.trim(), FORMATTER);
            endTime = LocalDateTime.parse(endStr.trim(), FORMATTER);
        } catch (DateTimeParseException e) {
            log.error("日期格式解析失敗, startStr: {}, endStr: {}", startStr, endStr, e);
            throw new ServiceException("日期格式錯誤，請使用 yyyy-MM-dd HH:mm:ss 格式");
        }

        // 取得當前系統時間作為基準點
        LocalDateTime now = LocalDateTime.now();

        // --- STEP 2: 日期區間範圍檢核 (60日限制) --- [cite: 14]
        // 1. 開始時間不得小於 60 日前 [cite: 15]
        if (startTime.isBefore(now.minusDays(60))) {
            throw new ServiceException("查詢期間僅能兩個月，請重新輸入"); // 
        }

        // 2. 結束時間不得大於 60 日後 [cite: 16]
        if (endTime.isAfter(now.plusDays(60))) {
            throw new ServiceException("查詢期間僅能兩個月，請重新輸入"); // 
        }

        // 3. 開始日 ~ 結束日不得超過 60 日 
        long daysBetween = ChronoUnit.DAYS.between(startTime, endTime);
        if (daysBetween < 0 || daysBetween > 60) {
            throw new ServiceException("查詢期間僅能兩個月，請重新輸入"); // 
        }

        // --- STEP 3: 檢核通過，調用 Mapper 進行多表聯查 --- [cite: 14, 20]
        log.info("執行約訪名單查詢，區間: {} ~ {}", startStr, endStr);
        List<AppointQueryResultDto> resultList = tbca003Mapper.queryUnfinishedAppointments(startTime, endTime);

        // --- STEP 4: 若查無資料，告警提示 --- [cite: 21, 22]
        if (resultList == null || resultList.isEmpty()) {
            throw new ServiceException("查無資料，請再確認"); // 
        }

        return resultList;
    }
}