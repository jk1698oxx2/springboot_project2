package com.example.reservation_platform.service;

import com.example.reservation_platform.dto.UpdateAppointReq;
import com.example.reservation_platform.dto.UpdateAppointResp;

public interface CustomerCommandService {
    
    /**
     * 批次/單筆異動約訪記錄檔
     * @param req 前端傳入的更新請求內容
     * @param currentUser 當前登入使用者的員編 (CathayNo)
     * @return 批次執行的統計結果與明細
     */
    UpdateAppointResp updateAppointments(UpdateAppointReq req, String currentUser);
}