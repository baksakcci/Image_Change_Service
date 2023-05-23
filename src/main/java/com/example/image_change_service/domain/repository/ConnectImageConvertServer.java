package com.example.image_change_service.domain.repository;

import com.example.image_change_service.dto.ConvertImageResponseDto;

public interface ConnectImageConvertServer {
    public ConvertImageResponseDto findConvertedImage(String filename, String type);
}
