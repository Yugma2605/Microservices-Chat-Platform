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
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@Service
@Data
@RequiredArgsConstructor
@Slf4j
public class FileStorageService {

    private final S3Client s3Client;

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
}
