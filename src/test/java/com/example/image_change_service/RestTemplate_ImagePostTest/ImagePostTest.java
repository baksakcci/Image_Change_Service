package com.example.image_change_service.RestTemplate_ImagePostTest;

import com.example.image_change_service.dto.ResponseDto;
import org.assertj.core.api.Assert;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;


@SpringBootTest
public class ImagePostTest {
    @Test
    @Disabled
    public void postRequestWithImage() {
        String url = "http://13.209.144.73:8080/api/image/upload";
        RestTemplate restTemplate = new RestTemplate();

        byte[] bytesImage = new byte[1024];
        try {
            // buffer 공간에 Image File을 담는다
            BufferedImage bufferedImage = ImageIO.read(new File("D:/projects/Spring Project/Image_Change_Service/photo/java.png"));
            // byte로 바꾸기 위한 outputStream 만들기. I/O 수행은 아니고 단지 byte로의 변환
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            // ImageIO를 사용하여 png 형식의 이미지 파일을 outputStream에 넣음
            ImageIO.write(bufferedImage, "png", outputStream);
            //
            bytesImage = outputStream.toByteArray();
            //
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        RequestEntity<?> request = RequestEntity.post(url)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(bytesImage);

        ResponseEntity<ResponseDto> exchange = restTemplate.exchange(request, ResponseDto.class);

        Assertions.assertThat(exchange.getBody().getMessage()).isEqualTo("File uploaded Successful!");
    }
}
