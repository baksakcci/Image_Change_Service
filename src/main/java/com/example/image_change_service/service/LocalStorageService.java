package com.example.image_change_service.service;

import com.amazonaws.util.IOUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
public class LocalStorageService {

    public byte[] storedObject(MultipartFile file) {
        byte[] image = new byte[0];
        try {
            InputStream inputStream = file.getInputStream();
            image = IOUtils.toByteArray(inputStream);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    public Object fetchObject(String awsFileName) {
        return null;
    }

    public void deleteObject(String key) {

    }
}
