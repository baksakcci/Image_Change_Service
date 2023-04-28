package com.example.image_change_service.controller;

import com.example.image_change_service.dto.ResponseDto;
import com.example.image_change_service.exception.ImageOverCapacityException;
import com.example.image_change_service.exception.NotImageFileException;
import com.example.image_change_service.exception.illegalArgumentImageException;
import com.example.image_change_service.service.AwsS3StorageService;
import com.example.image_change_service.service.LocalStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.HandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;

@RestController
@RequestMapping("/api/image")
@RequiredArgsConstructor
public class AwsS3Controller {

    private final AwsS3StorageService awsS3StorageService;

    @GetMapping("/health")
    public String healthCheck() {
        return "health";
    }

    @PostMapping(value = "/image_change")
    public ResponseEntity<?> imageChange(@RequestParam("image") MultipartFile file) {
        // validation
        if ((file.getContentType().equals("image/png")) && (file.getContentType().equals("image/jpg"))) {
            throw new NotImageFileException();
        }
        if (file.getSize() > 10000000) {
            throw new ImageOverCapacityException();
        }

        // store image
        awsS3StorageService.storedObject(file, file.getOriginalFilename(), file.getContentType());

        // request to AI server
        // body로 filename과 file형식을 보내주고 리턴값으로 inputStream
        /*

         */

        // load changed image
        byte[] bytes = awsS3StorageService.fetchObject(file.getOriginalFilename());


        // delete image (why? 초상권 보장, 이미지는 변환 이외에 다른 용도로 쓰지 않는다는 의미)
        // awsS3StorageService.deleteObject(file.getOriginalFilename());

        // return ResponseEntity
        ResponseDto resDto = new ResponseDto("이미지를 정상적으로 변환하였습니다.", bytes);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(resDto);
    }
}
