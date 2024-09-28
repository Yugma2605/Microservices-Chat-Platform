package com.example.messaging_service.dto;

import java.util.Date;

public record FileResponse(Long id,

                           String fileName,
                           String filePath,
                           String userId,
                           String username,
                           Long fileSize,
                           String fileType,
                           Date uploadDate){}
