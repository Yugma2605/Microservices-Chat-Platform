package com.example.auth_service.dto;

import lombok.Data;

@Data
public class AuthResponse {
    private String userName;
    private String userId;
    private String message;
    public AuthResponse(String userName,String userId, String message) {
        this.userName = userName;
        this.userId = userId;
        this.message = message;
    }
}
