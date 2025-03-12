package com.example.auth_service.service;

import com.example.auth_service.repository.AuthRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Data
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    @Autowired
    private AuthRepository authRepository;

}
