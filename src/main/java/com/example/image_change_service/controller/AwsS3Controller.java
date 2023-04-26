package com.example.image_change_service.controller;

import com.example.image_change_service.dto.ResponseDto;
import com.example.image_change_service.service.AwsS3StorageService;
import com.example.image_change_service.service.LocalStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
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

    @PostMapping(value = "/upload", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> upload(@RequestParam("image") MultipartFile file) {
        URL url = awsS3StorageService.storedObject(file, file.getOriginalFilename(), file.getContentType());

        String message = "File uploaded Successful!";
        ResponseDto responseDto = new ResponseDto(url, message);

        // responseDto convert 에러남 수정해야할듯
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return new ResponseEntity<>(responseDto, headers , HttpStatus.OK);
    }

    @PostMapping("/download")
    public ResponseEntity<?> download(@RequestBody String filename) {
        byte[] bytes = awsS3StorageService.fetchObject(filename);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        headers.setContentLength(bytes.length);

        return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
    }

    @PostMapping("/delete")
    public ResponseEntity<?> delete(@RequestBody String filename) {
        awsS3StorageService.deleteObject(filename);

        String message = "File deleted Successful!";
        ResponseDto responseDto = new ResponseDto(null, message);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return new ResponseEntity<>(responseDto, headers, HttpStatus.OK);
    }
}
