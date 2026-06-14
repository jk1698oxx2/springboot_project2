package com.example.reservation_platform.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateAppointReq {
    private List<UpdateAppointItemDto> reqList;
    private String arrangeType;
}