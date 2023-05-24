package com.example.image_change_service;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class ImageChangeServiceApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder(ImageChangeServiceApplication.class)
                .run(args);
    }
}
