package com.example.image_change_service.presentation.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@Getter
@Builder
public class FailureResponse {
    private int status;
    private String error;
    private String message;
    private LocalDateTime timestamp;

    public static FailureResponse create(ErrorCode errorCode) {
        return new FailureResponseBuilder()
                .status(errorCode.getHttpStatus().value())
                .error(errorCode.name())
                .message(errorCode.getLogs())
                .timestamp(LocalDateTime.now())
                .build();
    }
}
