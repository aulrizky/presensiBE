package com.tujuhsembilan.presensi79.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class CustomErrorWithStatusException extends RuntimeException {

    private HttpStatus status;
    private String errorMessage;

    public CustomErrorWithStatusException(String message, String errorMessage, HttpStatus status) {
        super(message);
        this.status = status;
        this.errorMessage = errorMessage;

    }
}
