package com.example.image_change_service.presentation.controller;

import com.example.image_change_service.domain.vo.Image;
import com.example.image_change_service.dto.ConvertImageResponseDto;
import com.example.image_change_service.dto.ResponseDto;

import com.example.image_change_service.presentation.exception.CustomException;
import com.example.image_change_service.presentation.exception.ErrorCode;
import com.example.image_change_service.service.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/image")
@RequiredArgsConstructor
@Slf4j
public class ImageConvertController {

    private final ImageService imageService;

    @GetMapping("/health")
    public String healthCheck() {
        return "health";
    }

    @PostMapping(value = "/image_change")
    public ResponseEntity<?> imageChange(
            @RequestParam("index") Integer index,
            @RequestParam("image") final MultipartFile multipartFile) {
        if ((index > 6) && (index <= 0)) {
            throw new CustomException(ErrorCode.INDEX_INVALID, "1~6 사이의 값 중 한개를 선택하세요");
        }
        String originalFilename = multipartFile.getOriginalFilename();
        log.info(originalFilename);
        log.info((originalFilename.substring(originalFilename.lastIndexOf("."))));
        if (!((originalFilename.substring(originalFilename.lastIndexOf(".")).equals(".png"))
                || (originalFilename.substring(originalFilename.lastIndexOf(".")).equals(".jpg")))) {
            throw new CustomException(ErrorCode.IMAGE_FILE_INVALID_TYPE, "파일 형식을 다시한번 확인하세요.");
        }
        if (multipartFile.getSize() > 10000000) {
            throw new CustomException(ErrorCode.IMAGE_FILE_SIZE_OVER, "파일 사이즈를 확인하세요.");
        }
        if (multipartFile.isEmpty()) {
            throw new CustomException(ErrorCode.IMAGE_FILE_IS_NULL, "이미지가 전달되지 않았습니다.");
        }

        Image image = Image.create(multipartFile);

        imageService.uploadOriginalImage(image);
        byte[] convertedImage = imageService.loadConvertedImage(image, index);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ResponseDto("이미지를 정상적으로 변환하였습니다.", convertedImage));
    }
}
