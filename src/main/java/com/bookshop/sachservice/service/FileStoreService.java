package com.bookshop.sachservice.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.bookshop.sachservice.constants.AwsConstants;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class FileStoreService {
    private final AmazonS3 amazonS3;

    public void upload(String path,
                       String fileName,
                       Optional<Map<String, String>> optionalMetadata,
                       InputStream inputStream) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        optionalMetadata.ifPresent(map -> {
            if(!map.isEmpty()) {
                map.forEach(objectMetadata::addUserMetadata);
            }
        });
        try {
            PutObjectRequest request = new PutObjectRequest(AwsConstants.S3_BUCKET_NAME, fileName, inputStream, objectMetadata);
            amazonS3.putObject(request);
        } catch (AmazonServiceException e) {
            e.printStackTrace();
            throw new IllegalStateException("Failed", e);
        }
    }
}
