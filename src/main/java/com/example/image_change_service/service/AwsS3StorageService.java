package com.example.image_change_service.service;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import com.example.image_change_service.exception.AWSS3ServerErrorException;
import com.example.image_change_service.exception.InternalServerErrorException;
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

    private final Logger logger = LoggerFactory.getLogger(AwsS3StorageService.class);

    // @Value는 필드나 생성자(매개변수)에다가 property 값을 읽어서 injection해주는 기능
    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    private final AmazonS3 amazonS3Client;

    public void storedObject(MultipartFile file, String fileName, String contentType) {
        // content-type, 파일길이 등 메타데이터 설정
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(contentType);
        objectMetadata.setContentLength(file.getSize());

        String key = bucketName + "/" + fileName;

        // 저장
        try {
            amazonS3Client.putObject(bucketName, fileName, file.getInputStream(), objectMetadata);

            if(amazonS3Client.doesObjectExist(bucketName, key)) {
                throw new InternalServerErrorException("Not File in Bucket");
            }
        } catch (AmazonServiceException e) {
            throw new AWSS3ServerErrorException(e.getErrorMessage());
        } catch (AmazonClientException | IOException e) {
            throw new InternalServerErrorException(e.getMessage());
        }

    }


    public byte[] fetchObject(String fileName) {
        byte[] bytes;
        try {
            S3Object s3Object = amazonS3Client.getObject(new GetObjectRequest(bucketName, fileName));
            S3ObjectInputStream content = s3Object.getObjectContent();
            bytes = IOUtils.toByteArray(content);
        } catch (AmazonServiceException e) {
            throw new AWSS3ServerErrorException(e.getErrorMessage());
        } catch (AmazonClientException | IOException e) {
            throw new InternalServerErrorException(e.getMessage());
        }
        return bytes;
    }

    public void deleteObject(String fileName) {
        String key = bucketName + "/" + fileName;
        try {
            amazonS3Client.deleteObject(new DeleteObjectRequest(bucketName, key));
        } catch (AmazonServiceException e) {
            e.printStackTrace();
            throw new AWSS3ServerErrorException(e.getErrorMessage());
        } catch (AmazonClientException e) {
            throw new InternalServerErrorException(e.getMessage());
        }
    }
}
