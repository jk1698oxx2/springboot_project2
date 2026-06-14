package com.example.reservation_platform.service;

import java.util.List;

import com.example.reservation_platform.dto.AppointQueryResultDto;

public interface CustomerQueryService {
    
    /**
     * 查詢尚未完成的約訪名單
     * @param startStr 前端傳入的開始時間 (格式: yyyy-MM-dd HH:mm:ss)
     * @param endStr   前端傳入的結束時間 (格式: yyyy-MM-dd HH:mm:ss)
     * @return 符合條件的約訪明細清單
     */
    List<AppointQueryResultDto> getUnfinishedAppointments(String startStr, String endStr);
}