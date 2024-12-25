package com.ecommerce.services.impl;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AwsS3ServiceImpl {

    @Value("${cloud.aws.s3.bucket-name}")
    private String bucketName;

    @Value("${cloud.aws.region.static}")
    private String awsRegion;

    @Value("${aws.s3.access}")
    private String awsS3AccessKey;

    @Value("${aws.s3.secrete}")
    private String awsS3SecreteKey;

    public String saveImageToS3(MultipartFile photo) {
        try {
            // Generate a unique S3 file name using the original file name
            String s3FileName = photo.getOriginalFilename();

            // Create AWS credentials using the access and secret keys
            BasicAWSCredentials awsCredentials = new BasicAWSCredentials(awsS3AccessKey, awsS3SecreteKey);

            // Create an S3 client with the configured credentials and region
            AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                    .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                    .withRegion(Regions.fromName(awsRegion)) // Use the correct region
                    .build();

            // Get the input stream from the uploaded photo
            InputStream inputStream = photo.getInputStream();

            // Set metadata for the uploaded object
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(photo.getContentType());
            metadata.setContentLength(photo.getSize());

            // Create a PUT request to upload the image to S3
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, s3FileName, inputStream, metadata);
            s3Client.putObject(putObjectRequest);

            // Return the URL of the uploaded file in S3
            return "https://" + bucketName + ".s3." + awsRegion + ".amazonaws.com/" + s3FileName;

        } catch (IOException e) {
            log.error("Error uploading image to S3 bucket: {}", e.getMessage(), e);
            throw new RuntimeException("Unable to upload image to S3 bucket: " + e.getMessage());
        }
    }
}
