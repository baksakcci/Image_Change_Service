package com.example.image_change_service.infra;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import com.example.image_change_service.presentation.exception.AWSS3ServerErrorException;
import com.example.image_change_service.presentation.exception.InternalServerErrorException;
import com.example.image_change_service.domain.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class AwsS3Storage implements ImageRepository {

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    private final AmazonS3 amazonS3Client;

    public void storedObject(MultipartFile file) {
        // content-type, 파일길이 등 메타데이터 설정
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(file.getContentType());
        objectMetadata.setContentLength(file.getSize());

        String fileName = file.getOriginalFilename();
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
            amazonS3Client.deleteObject(bucketName, key);
        } catch (AmazonServiceException e) {
            e.printStackTrace();
            throw new AWSS3ServerErrorException(e.getErrorMessage());
        } catch (AmazonClientException e) {
            throw new InternalServerErrorException(e.getMessage());
        }
    }
}
