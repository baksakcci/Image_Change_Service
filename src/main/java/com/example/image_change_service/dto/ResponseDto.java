package com.example.image_change_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.net.URL;

@AllArgsConstructor
@Getter
public class ResponseDto {
    private URL url;
    private String message;
}
