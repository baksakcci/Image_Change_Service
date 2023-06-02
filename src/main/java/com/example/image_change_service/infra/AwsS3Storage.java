package com.example.image_change_service.infra;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import com.example.image_change_service.domain.vo.Image;
import com.example.image_change_service.presentation.exception.CustomException;
import com.example.image_change_service.presentation.exception.ErrorCode;
import com.example.image_change_service.domain.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class AwsS3Storage implements ImageRepository {

    private final AmazonS3 amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    public void storedObject(Image image) {
        try {
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentType(image.getContentType());
            objectMetadata.setContentLength(image.getSize());
            String fileName = image.getFilename();

            InputStream imageInputStream = image.getImage().getInputStream();
            imageInputStream.close();
            amazonS3Client.putObject(bucketName, fileName, imageInputStream, objectMetadata);
        } catch (AmazonServiceException e) {
            throw new CustomException(ErrorCode.AMAZON_SERVER_ERROR, e.getErrorMessage());
        } catch (AmazonClientException | IOException e) {
            log.error(e.getMessage());
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
        try {
            amazonS3Client.deleteObject(bucketName, fileName);
        } catch (AmazonServiceException e) {
            throw new CustomException(ErrorCode.AMAZON_SERVER_ERROR, e.getErrorMessage());
        } catch (AmazonClientException e) {
            throw new CustomException(ErrorCode.AMAZON_CLIENT_ERROR, e.getLocalizedMessage());
        }
    }

    public boolean checkImageExists(Image image) {
        try {
            String imageHash = calculateImageHash(image.getImage().getInputStream());

            ObjectListing objectListing = amazonS3Client.listObjects(bucketName);
            List<S3ObjectSummary> objectSummaries = objectListing.getObjectSummaries();
            for (S3ObjectSummary objectSummary : objectSummaries) {
                String existingObjectHash = objectSummary.getETag();

                if (imageHash.equals(existingObjectHash)) {
                    log.info("S3 내에서 같은 파일을 찾았습니다!");
                    return true;
                }
            }
        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
        }
        log.info("같은 파일을 찾지 못했습니다.");
        return false;
    }

    private String calculateImageHash(InputStream inputStream) throws IOException {
        return DigestUtils.md5Hex(inputStream);
    }
}
