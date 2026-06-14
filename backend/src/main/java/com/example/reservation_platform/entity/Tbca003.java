package com.example.reservation_platform.entity;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tbca003 {
    private Integer sno;
    private String listNo;
    private String listCampcode;
    private LocalDateTime recallTime;
    private LocalDateTime recTime;
    private LocalDateTime updateTime;
    private String updateUser;
}