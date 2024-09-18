package com.tujuhsembilan.presensi79.exception;

import com.tujuhsembilan.presensi79.dto.MessageResponse;
import com.tujuhsembilan.presensi79.util.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@ControllerAdvice
public class CustomGlobalExceptionHandler {

    @Autowired
    private MessageUtil messageUtil;

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<MessageResponse> handleMaxSizeException(MaxUploadSizeExceededException exc) {
        MessageResponse response = new MessageResponse(
            messageUtil.get("application.error.uploadfile"),
            HttpStatus.BAD_REQUEST.value(),
            messageUtil.get("application.status.error")
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}
