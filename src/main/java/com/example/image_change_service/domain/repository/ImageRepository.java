package com.example.image_change_service.domain.repository;

import com.example.image_change_service.domain.entity.Image;
import org.springframework.web.multipart.MultipartFile;

public interface ImageRepository {
    public void storedObject(Image image);

    public byte[] fetchObject(String fileName);

    public void deleteObject(String fileName);
}
