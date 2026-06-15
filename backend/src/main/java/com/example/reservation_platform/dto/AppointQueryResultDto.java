package com.example.reservation_platform.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppointQueryResultDto {
    private String executionResult;
    private Integer sno;
    private String listNo;
    private String custName;
    private String campName;
    private LocalDateTime recallTime;
    private String listLastPhone;
    private LocalDate campServiceDt;
}