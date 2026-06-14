package com.example.reservation_platform.dto;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateAppointResp {
    private int totalCount;
    private int successCount;
    private int failCount;
    private Map<Integer, String> detailedResults;
}