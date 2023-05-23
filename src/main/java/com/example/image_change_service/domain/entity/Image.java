package com.example.image_change_service.domain.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Image {
    private final MultipartFile image;
    @Getter
    private final String filename;
    @Getter
    private final String contentType;

    public static Image create(MultipartFile image) {
        String filename = image.getOriginalFilename();
        String contentType = image.getContentType();
        return new Image(image, filename, contentType);
    }

    public MultipartFile getImageFile() {
        return image;
    }
}
