package com.example.image_change_service.domain.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class Image {
    private final InputStream image;
    private final String filename;
    private final String contentType;
    private final long size;

    public static Image create(MultipartFile multipartFile) throws IOException{
        InputStream image = multipartFile.getInputStream();
        String filename = multipartFile.getOriginalFilename();
        String contentType = multipartFile.getContentType();
        long size = multipartFile.getSize();
        return new Image(image, filename, contentType, size);
    }
}
