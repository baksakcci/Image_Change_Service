package com.example.image_change_service.service;

import org.springframework.web.multipart.MultipartFile;

import java.net.URL;

public interface StorageService {
    public Object storedObject(MultipartFile file, String fileName, String contentType);
    public Object fetchObject(String awsFileName);
    public void deleteObject(String key);
}
