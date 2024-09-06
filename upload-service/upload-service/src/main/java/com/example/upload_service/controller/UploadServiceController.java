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
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<FileResponse> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("userId") Long userId,
            @RequestParam("username") String username
            ){
        return new ResponseEntity<>(fileStorageService.uploadFile(file, userId, username), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<String> test(){
        return new ResponseEntity<>("HELLO", HttpStatus.OK);
    }
}
