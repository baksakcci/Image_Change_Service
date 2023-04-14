package com.example.image_change_service.dto;

import lombok.AllArgsConstructor;

import java.net.URL;

@AllArgsConstructor
public class ResponseDto {
    private URL url;
    private String message;
}
