package com.duongw.stayeasy.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data

public class ApiResponse<T> {
    private int status;
    private boolean success;
    private String message;
    private T data;
    private LocalDateTime timeStamp;

    public ApiResponse() {
        this.timeStamp = LocalDateTime.now();
    }

    public ApiResponse(boolean success, String message,int status, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.status = status;
        this.timeStamp = LocalDateTime.now();
    }

    public ApiResponse(boolean success, String message,int status ) {
        this.success = success;
        this.message = message;

        this.status = status;
        this.timeStamp = LocalDateTime.now();
    }
}
