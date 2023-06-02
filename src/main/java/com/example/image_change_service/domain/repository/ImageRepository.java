package com.example.image_change_service.domain.repository;

import com.example.image_change_service.domain.vo.Image;

public interface ImageRepository {
    public void storedObject(Image image);

    public byte[] fetchObject(String fileName);

    public void deleteObject(String fileName);

    public boolean checkImageExists(Image image);
}
