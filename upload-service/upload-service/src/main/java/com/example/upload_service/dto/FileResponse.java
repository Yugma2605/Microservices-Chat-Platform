package com.example.upload_service.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

import java.util.Date;

public record FileResponse(Long id,

 String fileName,
 String filePath,
 Long userId,
 String username,
 Long fileSize,
 String fileType,
 Date uploadDate){}