package com.example.image_change_service.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Service
public class LocalStorageService implements StorageService {
    @Override
    public FileOutputStream storedObject(MultipartFile file, String fileName, String contentType) {
        /*
        File localFile = new File("C://Users/" + file.getOriginalFilename());
        try {
            file.transferTo(localFile);
            FileOutputStream fileOutputStream = new FileOutputStream(localFile);
            return fileOutputStream;
        } catch (IOException e) {
            e.printStackTrace();
        }

         */
        return null;
    }

    @Override
    public Object fetchObject(String awsFileName) {
        return null;
    }

    @Override
    public void deleteObject(String key) {

    }
}
