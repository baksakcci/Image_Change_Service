package com.example.image_change_service.service;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;

@Component
@RequiredArgsConstructor
public class AwsS3StorageService {

    private static final Logger logger = LoggerFactory.getLogger(AwsS3StorageService.class);

    // @Value는 필드나 생성자(매개변수)에다가 property 값을 읽어서 injection해주는 기능
    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    @Qualifier("AmazonS3Client")
    private final AmazonS3 amazonS3Client;

    public URL storedObject(MultipartFile file, String fileName, String contentType) {
        // content-type, 파일길이 등 메타데이터 설정
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(contentType);
        objectMetadata.setContentLength(file.getSize());

        // 저장
        try {
            amazonS3Client.putObject(bucketName, fileName, file.getInputStream(), objectMetadata);
        } catch (AmazonClientException | IOException exception) {
            logger.error(exception.getLocalizedMessage());
        }

        // 저장된 S3 url 찾아오기
        return amazonS3Client.getUrl(bucketName, fileName);
    }

    public byte[] fetchObject(String fileName) {
        byte[] bytes = new byte[1024];
        try {
            S3Object s3Object = amazonS3Client.getObject(new GetObjectRequest(bucketName, fileName));
            S3ObjectInputStream content = s3Object.getObjectContent();
            bytes = IOUtils.toByteArray(content);
        } catch (AmazonServiceException serviceException) {
            logger.error(serviceException.getErrorMessage());
        } catch (AmazonClientException exception) {
            throw new RuntimeException("Error while streaming File.");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        return bytes;
    }

    // logger.error()와 throw new RuntimeException의 차이점
    public void deleteObject(String fileName) {
        String key = bucketName + "/" + fileName;
        try {
            amazonS3Client.deleteObject(bucketName, key);
        } catch (AmazonServiceException serviceException) {
            logger.error(serviceException.getErrorMessage());
        } catch (AmazonClientException exception) {
            logger.error("Error while deleting File.");
        }

    }
}
