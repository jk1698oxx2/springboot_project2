package com.example.reservation_platform.service.impl;

import com.example.reservation_platform.dto.UpdateAppointItemDto;
import com.example.reservation_platform.dto.UpdateAppointReq;
import com.example.reservation_platform.dto.UpdateAppointResp;
import com.example.reservation_platform.entity.Tbca003;
import com.example.reservation_platform.exception.ServiceException;
import com.example.reservation_platform.mapper.Tbca003Mapper;
import com.example.reservation_platform.service.CustomerCommandService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerCommandServiceImpl implements CustomerCommandService {

    private final Tbca003Mapper tbca003Mapper;

    @Override
    public UpdateAppointResp updateAppointments(UpdateAppointReq req, String currentUser) {
        
        // 基礎安全性檢核
        if (req == null || req.getReqList() == null || req.getReqList().isEmpty()) {
            throw new ServiceException("未勾選任何約訪案件，無法進行更新");
        }

        int totalCount = req.getReqList().size();
        int successCount = 0;
        int failCount = 0;
        
        // 用來記錄每筆 SNO 對應的執行結果 ("完成" 或 "失敗")，供前端渲染狀態
        Map<Integer, String> detailedResults = new HashMap<>();

        // 2.2 取得 UPDATE_TIME 為當前系統時間
        LocalDateTime now = LocalDateTime.now();

        log.info("開始處理批次約訪異動，操作人員: {}, 總筆數: {}, 安排方式: {}", currentUser, totalCount, req.getArrangeType());

        // 3.1 進行 a003 table 資料更新 (透過迴圈逐筆處理)
        for (UpdateAppointItemDto item : req.getReqList()) {
            Integer sno = item.getSno();
            LocalDateTime targetRecallTime = item.getRecallTime();

            try {
                // 1.1 檢查參數
                if (sno == null) {
                    throw new IllegalArgumentException("【流水號】不得為空");
                }
                if (targetRecallTime == null) {
                    throw new IllegalArgumentException("【約訪時間設定值】不得為空");
                }

                // 前端規格 4/20 新增：若為指定特定時間(安排方式"3")，需大於現在時間 5 分鐘後
                if ("3".equals(req.getArrangeType())) {
                    if (targetRecallTime.isBefore(now.plusMinutes(5))) {
                        throw new IllegalArgumentException("變更之約訪時間必須大於現在時間 5 分鐘後");
                    }
                }

                // 3.1.1 將 STEP 2 取得的欄位及[輸入參數]進行更新
                Tbca003 updateEntity = Tbca003.builder()
                        .sno(sno)
                        .recallTime(targetRecallTime)
                        .updateTime(now)        // 2.2.1 當前時間
                        .updateUser(currentUser) // 2.1.1 登入者員編
                        .build();

                int updatedRows = tbca003Mapper.updateAppoint(updateEntity);

                if (updatedRows > 0) {
                    successCount++;
                    detailedResults.put(sno, "完成");
                } else {
                    throw new RuntimeException("受影響列數為 0，可能該筆資料已被他人異動或刪除");
                }

            } catch (Exception e) {
                // 3.3 若執行流程中途有誤，則紀錄 error trace log
                log.error("流水號 [{}] 更新失敗，錯誤原因: ", sno, e);
                
                failCount++;
                detailedResults.put(sno, "失敗");
            }
        }

        log.info("批次約訪異動結束。成功: {} 筆, 失敗: {} 筆", successCount, failCount);
        
        // 封裝成最後要給 Controller 轉成 JSON 的格式 
        return UpdateAppointResp.builder()
                .totalCount(totalCount)
                .successCount(successCount)
                .failCount(failCount)
                .detailedResults(detailedResults)
                .build();
    }
}