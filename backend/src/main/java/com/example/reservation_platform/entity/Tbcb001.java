package com.example.reservation_platform.entity;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tbcb001 {
    private String campCode;
    private String campName;
    private LocalDate campServiceDt;
}
