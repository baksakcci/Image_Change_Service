package com.example.image_change_service.infra;

import com.example.image_change_service.domain.entity.Image;
import com.example.image_change_service.dto.ConvertImageResponseDto;
import com.example.image_change_service.domain.repository.ConnectImageConvertServer;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.nio.charset.Charset;


@Repository
@RequiredArgsConstructor
public class ConnectImageConvertFlaskServer implements ConnectImageConvertServer {

    private final String baseUrl = "http://localhost:5000/change";

    public ConvertImageResponseDto findConvertedImage(String filename, String type) {
        // query parameter 방식 사용
        UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .queryParam("filename", filename)
                .queryParam("type", type)
                .build(false);

        // Header 제작
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setConnectTimeout(5000); // api 호출 타임아웃
        factory.setReadTimeout(5000); // api 읽기 타임아웃

        RestTemplate restTemplate = new RestTemplate(factory);

        // 응답
        ConvertImageResponseDto response = restTemplate.getForObject(uriComponents.toUriString(), ConvertImageResponseDto.class);

        return response;
    }
}
