package com.example.reservation_platform.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.reservation_platform.vo.ReturnMsg;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 專門攔截你寫好的 ServiceException
     */
    @ExceptionHandler(ServiceException.class)
    public ReturnMsg<Void> handleServiceException(ServiceException e) {
        log.warn("業務邏輯檢核未通過: {}", e.getMessage());
        // 直接回傳規格書指定的 returnCode: "-1"，並帶入你寫在 Service 的錯誤訊息 
        return ReturnMsg.fail(e.getMessage());
    }

    /**
     * 攔截其他未預期的系統錯誤 (例如資料庫斷線、空指標等)
     */
    @ExceptionHandler(Exception.class)
    public ReturnMsg<Void> handleException(Exception e) {
        log.error("系統發生未預期異常: ", e);
        return ReturnMsg.fail("更新失敗"); // 遵循規格書 Step 4 的失敗預設文字 
    }
}