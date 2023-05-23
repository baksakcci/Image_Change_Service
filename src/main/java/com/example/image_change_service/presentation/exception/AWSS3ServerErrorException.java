package com.example.image_change_service.presentation.exception;

public class AWSS3ServerErrorException extends RuntimeException {
    public AWSS3ServerErrorException(String message) {
        super(message);
    }
}
