package com.example.image_change_service.service;

import com.example.image_change_service.dto.ConvertImageResponseDto;
import com.example.image_change_service.domain.repository.ConnectImageConvertServer;
import com.example.image_change_service.domain.repository.ImageRepository;
import com.example.image_change_service.domain.entity.Image;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;

    private final ConnectImageConvertServer connectImageConvertServer;

    public ConvertImageResponseDto sendImageToAIServer(MultipartFile multipartFile) throws IOException{
        Image image = Image.create(multipartFile);

        imageRepository.storedObject(image);

        ConvertImageResponseDto convertedImage = connectImageConvertServer
                .findConvertedImage(image.getFilename(), image.getContentType());

        return convertedImage;
    }

    public byte[] loadConvertedImage(ConvertImageResponseDto convertImageResponseDto) {
        String convertedImageFilename = convertImageResponseDto.getFilename();
        byte[] bytes = imageRepository.fetchObject(convertedImageFilename);
        // imageRepository.deleteObject(file.getOriginalFilename());
        return bytes;
    }
}
