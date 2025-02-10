package com.example.upload_service.controller;

import com.example.upload_service.dto.FileResponse;
import com.example.upload_service.service.FileStorageService;
import jakarta.persistence.Table;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/api/upload")
@Slf4j
public class UploadServiceController {

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private RestTemplate restTemplate;

    private final String AUTH_SERVICE_URL = "http://localhost:8085/api/auth/validate";

    @PostMapping("/uploadFile")
    @CrossOrigin(origins = "http://localhost:63342")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<FileResponse> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("userId") Long userId,
            @RequestParam("username") String username
            ){
        return new ResponseEntity<>(fileStorageService.uploadFile(file, userId, username), HttpStatus.OK);
    }
//    public ResponseEntity<FileResponse> uploadFile(
//            @RequestHeader("Authorization") String authHeader,
//            @RequestParam("file") MultipartFile file,
//            @RequestParam("userId") Long userId,
//            @RequestParam("username") String username) {
//        // Call Auth Service to validate the token
//        try {
//            // Prepare headers for the Auth Service request
//            HttpHeaders headers = new HttpHeaders();
//            headers.set("Authorization", authHeader);
//
//            HttpEntity<Void> entity = new HttpEntity<>(headers);
//
//            // Call the Auth Service
//            ResponseEntity<String> authResponse = restTemplate.exchange(
//                    AUTH_SERVICE_URL,
//                    HttpMethod.POST,
//                    entity,
//                    String.class
//            );
//            log.info("Auth Response: " + authResponse);
//
//            // Check if the Auth Service response indicates success
//            if (!authResponse.getStatusCode().is2xxSuccessful()) {
//                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
//            }
//
//        } catch (Exception e) {
//            log.info("Auth Header: " + authHeader);
//            log.error("Exception during token validation: " + e);
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
//        }
//
//        // If validation passes, proceed with upload logic
//        FileResponse fileResponse = fileStorageService.uploadFile(file, userId, username);
//        return new ResponseEntity<>(fileResponse, HttpStatus.CREATED);
//    }

    @CrossOrigin(origins = "http://localhost:63342")
    @GetMapping("/download")
    public ResponseEntity<String> generateSignedUrl(
            @RequestParam("fileUrl") String fileUrl) {
        try {
            String signedUrl = fileStorageService.createPresignedGetUrl(fileUrl);
            return new ResponseEntity<>(signedUrl, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @CrossOrigin(origins = "http://localhost:63342")
    @GetMapping("/hello")
    public ResponseEntity<String> test(
            ) {
        return new ResponseEntity<>("hello", HttpStatus.OK);
    }


}
