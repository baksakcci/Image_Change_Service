package com.example.image_change_service.domain.repository;

import org.springframework.web.multipart.MultipartFile;

public interface ImageRepository {
    public void storedObject(MultipartFile file);

    public byte[] fetchObject(String fileName);

    public void deleteObject(String fileName);
}
