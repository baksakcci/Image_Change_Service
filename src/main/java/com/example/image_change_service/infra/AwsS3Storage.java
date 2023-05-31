package com.example.image_change_service.infra;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import com.example.image_change_service.domain.entity.Image;
import com.example.image_change_service.presentation.exception.CustomException;
import com.example.image_change_service.presentation.exception.ErrorCode;
import com.example.image_change_service.domain.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class AwsS3Storage implements ImageRepository {

    private final AmazonS3 amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    public void storedObject(Image image) {
        // content-type, 파일길이 등 메타데이터 설정
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(image.getContentType());
        objectMetadata.setContentLength(image.getSize());

        String fileName = image.getFilename();
        String key = bucketName + "/" + fileName;

        try {
            amazonS3Client.putObject(bucketName, fileName, image.getImage(), objectMetadata);
            if(amazonS3Client.doesObjectExist(bucketName, key)) { // 파일 이름을 확인해서 이미 존재하는 파일인지 확인
                throw new CustomException(ErrorCode.AMAZON_CLIENT_ERROR, "not Found");
            }
        } catch (AmazonServiceException e) {
            throw new CustomException(ErrorCode.AMAZON_SERVER_ERROR, e.getErrorMessage());
        } catch (AmazonClientException e) {
            throw new CustomException(ErrorCode.AMAZON_CLIENT_ERROR, e.getLocalizedMessage());
        }
    }


    public byte[] fetchObject(String fileName) {
        byte[] bytes;
        try {
            S3Object s3Object = amazonS3Client.getObject(new GetObjectRequest(bucketName, fileName));
            S3ObjectInputStream content = s3Object.getObjectContent();
            bytes = IOUtils.toByteArray(content);
        } catch (AmazonServiceException e) {
            throw new CustomException(ErrorCode.AMAZON_SERVER_ERROR, e.getErrorMessage());
        } catch (AmazonClientException | IOException e) {
            throw new CustomException(ErrorCode.AMAZON_CLIENT_ERROR, e.getLocalizedMessage());
        }
        return bytes;
    }

    public void deleteObject(String fileName) {
        String key = bucketName + "/" + fileName;
        try {
            amazonS3Client.deleteObject(bucketName, key);
        } catch (AmazonServiceException e) {
            throw new CustomException(ErrorCode.AMAZON_SERVER_ERROR, e.getErrorMessage());
        } catch (AmazonClientException e) {
            throw new CustomException(ErrorCode.AMAZON_CLIENT_ERROR, e.getLocalizedMessage());
        }
    }
}
