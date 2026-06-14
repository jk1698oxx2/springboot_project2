package com.example.reservation_platform.controller;

import com.example.reservation_platform.dto.AppointQueryResultDto;
import com.example.reservation_platform.dto.UpdateAppointReq;
import com.example.reservation_platform.dto.UpdateAppointResp;
import com.example.reservation_platform.service.CustomerCommandService;
import com.example.reservation_platform.service.CustomerQueryService;
import com.example.reservation_platform.vo.ReturnMsg;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerQueryService customerQueryService;
    private final CustomerCommandService customerCommandService;

    @GetMapping("/queryUnfinished")
    public ReturnMsg<List<AppointQueryResultDto>> queryUnfinished(
            @RequestParam("startTime") String startTime,
            @RequestParam("endTime") String endTime) {
        
        List<AppointQueryResultDto> data = customerQueryService.getUnfinishedAppointments(startTime, endTime);
        
        return ReturnMsg.success("查詢成功", data);
    }

    @PostMapping("/updateAppoint")
    public ReturnMsg<UpdateAppointResp> updateAppoint(@RequestBody UpdateAppointReq req) { 
        
        String mockUserNo = "A12345"; 
        
        UpdateAppointResp respDto = customerCommandService.updateAppointments(req, mockUserNo);
        
        return ReturnMsg.success("更新成功", respDto);
    }
}