package com.example.upload_service.dto;

import lombok.Data;

@Data
public class FileResponse {
    private String fileName;
    private String message;

    public FileResponse(String fileName, String message) {
        this.fileName = fileName;
        this.message = message;
    }

    // Getters and setters
}
