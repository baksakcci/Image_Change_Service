package com.example.image_change_service.domain.vo;

import com.example.image_change_service.presentation.exception.CustomException;
import com.example.image_change_service.presentation.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class Image {
    private final MultipartFile image;
    private final String filename;
    private final String contentType;
    private final long size;

    public static Image create(MultipartFile multipartFile) {
        String filename = multipartFile.getOriginalFilename();
        String contentType = multipartFile.getContentType();
        long size = multipartFile.getSize();
        return new Image(multipartFile, filename, contentType, size);
    }

    public String convertName(String filename, Integer index) {
        String[] split = filename.split("\\.");
        String convert = split[0] + index.toString() + "." + split[1];
        return convert;
    }

    public String convertName2(String filename) {
        String[] split = filename.split("\\.");
        String convert = split[0] + "changed" + "." + split[1];
        return convert;
    }
}
