package com.example.image_change_service.dto;

import com.example.image_change_service.exception.ImageOverCapacityException;
import com.example.image_change_service.exception.NotImageFileException;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class ImageRequestDto {

    private MultipartFile image;

    public void validateImage() {
        checkSupportedExtension(image.getContentType());
        checkLowerFileSize(image.getSize());
    }

    private void checkSupportedExtension(String extension) {
        if ((extension.equals("image/png")) && (extension.equals("image/jpg"))) {
            throw new NotImageFileException();
        }
    }

    private void checkLowerFileSize(long fileSize) {
        if (fileSize > 10000000) {
            throw new ImageOverCapacityException();
        }
    }
}
