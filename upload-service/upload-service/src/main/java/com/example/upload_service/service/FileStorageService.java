package com.example.upload_service.service;

import com.example.upload_service.dto.FileResponse;
import com.example.upload_service.entity.FileMetaData;
import com.example.upload_service.repository.FileMetadataRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.io.IOException;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
@Service
@Data
@RequiredArgsConstructor
@Slf4j
public class FileStorageService {

    private final S3Client s3Client;

    private final S3Presigner s3Presigner;

    private String bucketName;

    private String region;

    @Autowired
    private FileMetadataRepository fileMetadataRepository;

    @Autowired
    public FileStorageService(
            @Value("${aws.s3.bucket-name}") String bucketName,
            @Value("${aws.s3.region}") String region,
            @Value("${aws.s3.access-key}") String accessKey,
            @Value("${aws.s3.secret-key}") String secretKey) {

        this.s3Client = S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(accessKey, secretKey)
                ))
                .build();

        this.s3Presigner = S3Presigner.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(accessKey, secretKey)
                ))
                .build();

        this.region = region;
        this.bucketName = bucketName;
    }

    public FileResponse uploadFile(MultipartFile file, Long userId, String username) {
        if (file == null || file.isEmpty()) {
            log.error("Failed to upload file: File is empty or null");
            throw new RuntimeException("File is empty or null");
        }

        String originalFileName = Paths.get(Objects.requireNonNull(file.getOriginalFilename())).getFileName().toString();
        String uniqueFileName = UUID.randomUUID().toString() + "_" + originalFileName;

        try {
            // Upload the file to S3
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(uniqueFileName)
                    .build();

            PutObjectResponse response = s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));
            log.info("S3 upload response: {}", response);

            String fileUrl = String.format("https://%s.s3.%s.amazonaws.com/%s", bucketName, region, uniqueFileName);
            FileMetaData fileMetadata = FileMetaData.builder()
                    .userId(userId)
                    .fileName(originalFileName)
                    .filePath(fileUrl)
                    .userId(userId)
                    .username(username)
                    .fileSize(file.getSize())
                    .fileType(file.getContentType())
                    .uploadDate(new Date())
                    .build();

            fileMetadataRepository.save(fileMetadata);

            return new FileResponse(fileMetadata.getId(),
                    fileMetadata.getFileName(),
                    fileMetadata.getFilePath(),
                    fileMetadata.getUserId(),
                    fileMetadata.getUsername(),
                    fileMetadata.getFileSize(),
                    fileMetadata.getFileType(),
                    fileMetadata.getUploadDate());

        } catch (IOException e) {
            log.error("Failed to upload file to S3: {}", e.getMessage());
            throw new RuntimeException("Failed to upload file to S3", e);
        }
    }

    public String extractFileKeyFromUrl(String s3Url) {
        String bucketUrlPrefix = String.format("https://%s.s3.%s.amazonaws.com/", bucketName, region);
        if (s3Url.startsWith(bucketUrlPrefix)) {
            return s3Url.substring(bucketUrlPrefix.length());
        } else {
            throw new IllegalArgumentException("Provided URL does not match S3 bucket URL pattern");
        }
    }

    public String createPresignedGetUrl(String s3Url) {
        try (S3Presigner presigner = S3Presigner.builder()
                .region(Region.of(region)) // Change this to your bucket's region
                .build()) {
            log.info(s3Url);
            String fileKey = extractFileKeyFromUrl(s3Url);

            GetObjectRequest objectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileKey)
                    .build();

            GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofMinutes(10))  // The URL will expire in 10 minutes.
                    .getObjectRequest(objectRequest)
                    .build();

            PresignedGetObjectRequest presignedRequest = presigner.presignGetObject(presignRequest);
            log.info("Presigned URL: [{}]", presignedRequest.url().toString());
            log.info("HTTP method: [{}]", presignedRequest.httpRequest().method());

            return presignedRequest.url().toExternalForm();
        }
    }

}
