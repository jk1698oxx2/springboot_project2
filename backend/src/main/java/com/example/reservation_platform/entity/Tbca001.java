package com.example.reservation_platform.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tbca001 {
    private String listNo;
    private String custNo;
    private String listLastPhone;
}