package com.example.upload_service.service;

import com.example.upload_service.repository.FileMetadataRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Objects;

@Service
@Data
public class FileStorageService {

    private final S3Client s3Client;

    private String bucketName;


    private String region;


    @Autowired
    private FileMetadataRepository fileMetadataRepository;

    // Constructor Injection for S3Client with correct access to all values
    @Autowired
    public FileStorageService(
            @Value("${aws.s3.bucket-name}") String bucketName,
            @Value("${aws.s3.region}") String region,
            @Value("${aws.s3.access-key}") String accessKey,
            @Value("${aws.s3.secret-key}") String secretKey) {

        // Initialize the S3Client with region and credentials
        this.s3Client = S3Client.builder()
                .region(Region.of(region)) // Use the region from the constructor parameter
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(accessKey, secretKey)
                ))
                .build();

        // Set the region field to ensure it's accessible in other methods
        this.region = region;
        this.bucketName = bucketName;
    }

    public String uploadFile(MultipartFile file, Long userId, String username) {

        String fileName = Paths.get(Objects.requireNonNull(file.getOriginalFilename())).getFileName().toString();
        try {
            // Upload the file to S3
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));

            // Correctly format the URL using the instance field for region
            return String.format("https://%s.s3.%s.amazonaws.com/%s", bucketName, region, fileName);
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload file to S3", e);
        }
    }
}
