package com.tujuhsembilan.presensi79.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MessageResponse {
    private String message;
    private int statusCode;
    private String status;
    private Object data;
    private String errorMessage;

    // Constructor for success response without data
    public MessageResponse(String message, int statusCode, String status) {
        this.message = message;
        this.statusCode = statusCode;
        this.status = status;
    }

    // Constructor for success response with data
    public MessageResponse(String message, int statusCode, String status, Object data) {
        this.message = message;
        this.statusCode = statusCode;
        this.status = status;
        this.data = data;
    }

    // Constructor for error response
    public MessageResponse(String message, int statusCode, String status, String errorMessage) {
        this.message = message;
        this.statusCode = statusCode;
        this.status = status;
        this.errorMessage = errorMessage;
    }
}
