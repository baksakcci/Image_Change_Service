package com.example.image_change_service.service;

import com.example.image_change_service.dto.ConvertImageResponseDto;
import com.example.image_change_service.domain.repository.ConnectImageConvertServer;
import com.example.image_change_service.domain.repository.ImageRepository;
import com.example.image_change_service.domain.entity.Image;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;

    private final ConnectImageConvertServer connectImageConvertServer;

    public ConvertImageResponseDto sendImageToAIServer(MultipartFile imageFile) {
        Image image = Image.create(imageFile);

        imageRepository.storedObject(image.getImageFile());

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
