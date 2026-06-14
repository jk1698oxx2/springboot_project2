package com.example.reservation_platform.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReturnMsg<T> {
    private String returnCode;
    private String msg;
    private T data;

    public static <T> ReturnMsg<T> success(String msg, T data) {
        return ReturnMsg.<T>builder()
                .returnCode("0")
                .msg(msg)
                .data(data)
                .build();
    }

    public static <T> ReturnMsg<T> fail(String msg) {
        return ReturnMsg.<T>builder()
                .returnCode("-1")
                .msg(msg)
                .data(null)
                .build();
    }
}