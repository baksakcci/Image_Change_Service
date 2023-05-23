package com.example.image_change_service.presentation.advice;

import com.example.image_change_service.presentation.exception.*;
import com.example.image_change_service.response.FailureResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(illegalArgumentImageException.class)
    public ResponseEntity illegalArgumentImageExceptionAdvice(IllegalStateException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new FailureResponse(401,"이미지를 넣지 않았습니다. 다시 시도해주세요", e.getMessage()));
    }

    @ExceptionHandler(NotImageFileException.class)
    public ResponseEntity NotImageFileExceptionAdvice(NotImageFileException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new FailureResponse(401, "이미지 형식이 아닙니다.", e.getMessage()));
    }

    @ExceptionHandler(ImageOverCapacityException.class)
    public ResponseEntity ImageOverCapacityExceptionAdvice(ImageOverCapacityException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new FailureResponse(401, "이미지 파일 크기를 10MB 이내로 보내주세요.", e.getMessage()));
    }

    @ExceptionHandler(InternalServerErrorException.class)
    // 파일을 S3에 전달하는데 I/O exception 이 발생했을 경우 ... Client 문제
    public ResponseEntity InternalServerErrorExceptionAdvice(InternalServerErrorException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new FailureResponse(500, "이미지를 S3에 저장하는 과정에서 생긴 오류입니다. " +
                        "잠시 후 다시 보내주세요.", e.getMessage()));
    }

    @ExceptionHandler(AWSS3ServerErrorException.class)
    public ResponseEntity AWSS3ServerErrorExceptionAdvice(AWSS3ServerErrorException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new FailureResponse(500, "이미지를 저장/삭제 도중 AWS S3 서비스에 " +
                        "이상이 생겼습니다.", e.getMessage()));
    }
}
