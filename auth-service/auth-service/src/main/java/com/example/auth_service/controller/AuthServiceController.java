package com.example.auth_service.controller;

import com.example.auth_service.entity.Users;
import com.example.auth_service.service.AuthService;
import com.example.auth_service.service.MyUserDetailsSevice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

@RestController
public class AuthServiceController {

    @Autowired
    private AuthService authService;

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

}







