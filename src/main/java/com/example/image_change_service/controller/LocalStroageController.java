package com.example.image_change_service.controller;

import com.amazonaws.util.IOUtils;
import com.example.image_change_service.service.LocalStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Controller
@RequestMapping("/api/image/local/")
@RequiredArgsConstructor
public class LocalStroageController {
    private final LocalStorageService localStorageService;

    @PostMapping(path = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<byte[]> imageUploadTest(@RequestParam("image") MultipartFile file) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        headers.setCacheControl(CacheControl.noCache().getHeaderValue());

        InputStream inputStream = null;
        byte[] image = null;
        try{
            inputStream = file.getInputStream();
            image = IOUtils.toByteArray(inputStream);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(image, headers, HttpStatus.OK);
    }
}
