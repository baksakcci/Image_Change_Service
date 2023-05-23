package com.example.image_change_service.presentation.controller;

import com.example.image_change_service.dto.ConvertImageResponseDto;
import com.example.image_change_service.dto.ResponseDto;

import com.example.image_change_service.presentation.exception.ImageOverCapacityException;
import com.example.image_change_service.presentation.exception.NotImageFileException;
import com.example.image_change_service.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/image")
@RequiredArgsConstructor
public class ImageConvertController {

    private final ImageService imageService;

    @GetMapping("/health")
    public String healthCheck() {
        return "health";
    }

    @PostMapping(value = "/image_change")
    public ResponseEntity<?> imageChange(@RequestParam("image") final MultipartFile image) {

        if ((image.getOriginalFilename().equals("image/png")) && (image.getOriginalFilename().equals("image/jpg"))) {
            throw new NotImageFileException();
        }
        if (image.getSize() > 10000000) {
            throw new ImageOverCapacityException();
        }

        ConvertImageResponseDto convertImage = imageService.sendImageToAIServer(image);
        byte[] convertedImage = imageService.loadConvertedImage(convertImage);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ResponseDto("이미지를 정상적으로 변환하였습니다.", convertedImage));
    }
}
