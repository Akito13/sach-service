package com.bookshop.sachservice.constants;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AwsConstants {

    public static String S3_ACCESS_KEY;
    public static String S3_SECRET_KEY;
    public static String S3_BUCKET_NAME;
    public static String S3_REGION;

    @Value("${aws.credentials.s3.access-key}")
    public void setS3AccessKey(String accessKey) {
        AwsConstants.S3_ACCESS_KEY = accessKey;
    }

    @Value("${aws.credentials.s3.secret-key}")
    public void setS3SecretKey(String secretKey) {
        AwsConstants.S3_SECRET_KEY = secretKey;
    }

    @Value("${aws.credentials.s3.bucket-name}")
    public void setS3BucketName(String bucketName) {
        AwsConstants.S3_BUCKET_NAME = bucketName;
    }

    @Value("${aws.credentials.s3.region}")
    public void setS3Region(String region) {
        AwsConstants.S3_REGION = region;
    }
}
