package com.example.image_change_service.presentation.exception;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    // 400
    IMAGE_FILE_INVALID_TYPE(HttpStatus.BAD_REQUEST, "이미지 파일 형식이 유효하지 않습니다. jpg나 png인지 확인하세요"),
    IMAGE_FILE_SIZE_OVER(HttpStatus.BAD_REQUEST, "이미지 파일 크기가 10MB가 넘습니다."),
    IMAGE_FILE_IS_NULL(HttpStatus.BAD_REQUEST, "이미지 파일이 전달되지 않았습니다."),
    INDEX_INVALID(HttpStatus.BAD_REQUEST, "1~6까지 예제 이미지를 선택해야 합니다."),

    // 500
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "이미지 서버에 문제가 생겼습니다."),
    AMAZON_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "AWS S3가 작동하지 않습니다"),
    AMAZON_CLIENT_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "AWS S3와의 통신에 실패하였습니다"),
    FLASK_SERVER_REQUEST_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "이미지 변환 서버와 통신이 원활하지 않습니다.");

    private final HttpStatus httpStatus;

    private final String logs;

}
