package com.example.image_change_service.configure;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebCorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://125.181.173.90:8080")
                .allowedMethods("GET", "POST", "OPTIONS")
                .allowCredentials(true)
                .maxAge(3000);
    }
}
