package com.example.auth_service.controller;

import com.example.auth_service.entity.Users;
import com.example.auth_service.service.AuthService;
import com.example.auth_service.service.JWTService;
import com.example.auth_service.service.MyUserDetailsSevice;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

@RestController
@Slf4j
@RequestMapping("/api/auth")
public class AuthServiceController {

    @Autowired
    private AuthService authService;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private MyUserDetailsSevice userService;


    @PostMapping("/login")
    public String login(@RequestBody Users user){
        return userService.verify(user);
    }

    @PostMapping("/register")
    public Users createUser(@RequestBody Users user){
        return userService.Register(user);
    }

    @GetMapping("/verifyUser")
    public ResponseEntity<String> test(){
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

    @GetMapping("/me")
    public UserDetails getCurrentUser() {
        // Get the authenticated user from SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Check if the authentication object contains the user details
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            return (UserDetails) authentication.getPrincipal();  // Return the user details
        } else {
            throw new RuntimeException("User is not authenticated");
        }
    }
    @PostMapping("/validate")
    public ResponseEntity<String> validateToken(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return new ResponseEntity<>("Invalid or missing Authorization header", HttpStatus.BAD_REQUEST);
        }

        String token = authHeader.substring(7);
//        log.info("token : "+token);
        String username = jwtService.extractUserName(token);
//        log.info("username : "+username);
        if (username == null) {
            return new ResponseEntity<>("Invalid token", HttpStatus.UNAUTHORIZED);
        }

        UserDetails userDetails = userService.loadUserByUsername(username);
//        log.info("userDetails : " + userDetails);
        if (jwtService.validateToken(token, userDetails)) {
            return new ResponseEntity<>("Token is valid", HttpStatus.OK);
        }

        return new ResponseEntity<>("Token is invalid or expired", HttpStatus.UNAUTHORIZED);
    }
}







