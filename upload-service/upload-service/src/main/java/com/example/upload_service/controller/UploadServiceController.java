package com.example.upload_service.controller;

import com.example.upload_service.dto.FileResponse;
import com.example.upload_service.service.FileStorageService;
import jakarta.persistence.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/api/upload")
public class UploadServiceController {

    @Autowired
    private FileStorageService fileStorageService;

    @PostMapping
    @CrossOrigin(origins = "http://localhost:63342")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<FileResponse> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("userId") Long userId,
            @RequestParam("username") String username
            ){
        return new ResponseEntity<>(fileStorageService.uploadFile(file, userId, username), HttpStatus.OK);
    }

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
