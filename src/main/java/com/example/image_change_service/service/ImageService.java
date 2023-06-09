package com.example.image_change_service.service;

import com.example.image_change_service.dto.ConvertImageResponseDto;
import com.example.image_change_service.domain.repository.ConnectImageConvertServer;
import com.example.image_change_service.domain.repository.ImageRepository;
import com.example.image_change_service.domain.vo.Image;
import com.example.image_change_service.presentation.exception.CustomException;
import com.example.image_change_service.presentation.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImageService {

    private final ImageRepository imageRepository;
    private final ConnectImageConvertServer connectImageConvertServer;

    public ConvertImageResponseDto sendImageToAIServer(Image image){
        ConvertImageResponseDto convertedImage = new ConvertImageResponseDto();
        if(imageRepository.checkImageExists(image)) {
            convertedImage.setFilename(image.getFilename());
            convertedImage.setType(image.getContentType());
        } else {
            imageRepository.storedObject(image);
            convertedImage = connectImageConvertServer
                            .findConvertedImage(image.getFilename(), image.getContentType());
        }
        return convertedImage;
    }

    public void uploadOriginalImage(Image image) {
        if(!imageRepository.checkImageExists(image)) {
            imageRepository.storedObject(image);
        }
    }

    public byte[] loadConvertedImage(Image image, Integer index) {
        String filename = image.convertName(image.getImage().getOriginalFilename(), index);
        // String filename = image.convertName2(image.getImage().getOriginalFilename());
        log.info("변경된 파일 이름: " + filename);
        byte[] bytes = imageRepository.fetchObject(filename);
        return bytes;
    }
}
