package com.example.image_change_service.presentation.controller;

import com.example.image_change_service.presentation.exception.*;
import com.example.image_change_service.presentation.exception.FailureResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ExceptionAdvice {
    @ExceptionHandler(CustomException.class)
    public ResponseEntity CustomExceptionHandler(CustomException e) {
        log.error(e.getErrorCode().name() + " " + e.getErrorMessage());
        ErrorCode errorCode = e.getErrorCode();
        FailureResponse failureResponse = FailureResponse.create(errorCode);
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(failureResponse);
    }
}
