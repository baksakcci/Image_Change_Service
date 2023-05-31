package com.example.image_change_service.service;

import com.example.image_change_service.dto.ConvertImageResponseDto;
import com.example.image_change_service.domain.repository.ConnectImageConvertServer;
import com.example.image_change_service.domain.repository.ImageRepository;
import com.example.image_change_service.domain.entity.Image;
import com.example.image_change_service.presentation.exception.CustomException;
import com.example.image_change_service.presentation.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;

    private final ConnectImageConvertServer connectImageConvertServer;

    public ConvertImageResponseDto sendImageToAIServer(MultipartFile multipartFile){
        Image image;
        try {
            image = Image.create(multipartFile);
        } catch (IOException e) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR, e.getLocalizedMessage());
        }
        imageRepository.storedObject(image);

        ConvertImageResponseDto convertedImage = connectImageConvertServer
                .findConvertedImage(image.getFilename(), image.getContentType());

        return convertedImage;
    }

    public byte[] loadConvertedImage(ConvertImageResponseDto convertImageResponseDto) {
        String convertedImageFilename = convertImageResponseDto.getFilename();
        byte[] bytes = imageRepository.fetchObject(convertedImageFilename);
        return bytes;
    }
}
