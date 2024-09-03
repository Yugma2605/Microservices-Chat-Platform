package com.example.upload_service.controller;

import com.example.upload_service.dto.FileResponse;
import com.example.upload_service.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/api/upload")
public class UploadServiceController {

    @Autowired
    private FileStorageService fileStorageService;

    @PostMapping("/file")
    public ResponseEntity<FileResponse> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("userId") Long userId,
            @RequestParam("username") String username
            ){
        String fileUrl = fileStorageService.uploadFile(file, userId, username);
        return new ResponseEntity<>(new FileResponse(fileUrl, "File uploaded successfully to S3"), HttpStatus.OK);
    }

    @GetMapping("/file")
    public ResponseEntity<String> test(){
        return new ResponseEntity<>("HELLO", HttpStatus.OK);
    }
}
