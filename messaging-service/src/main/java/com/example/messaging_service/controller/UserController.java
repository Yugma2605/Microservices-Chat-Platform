package com.example.messaging_service.controller;

import com.example.messaging_service.service.UserService;
import com.example.messaging_service.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/messaging")
@Slf4j
public class UserController {
    private final UserService userService;
    @MessageMapping("/user.addUser")
    @SendTo("/user/topic")
    public User addUser(@Payload User user){
        log.info("user found: "+user.getEmail());
        userService.saveUser(user);
        return user;
    }

    @MessageMapping("/user.disconnectUser")
    @SendTo("/user/topic")
    public User disconnect(@Payload User user){
        userService.disconnect(user);
        return user;
    }

    @GetMapping("/users")
//    @CrossOrigin(origins = "http://localhost:63342")
    public ResponseEntity<List<User>> findConnectedUsers(){
        return ResponseEntity.ok(userService.findConnectedUsers());
    }
}
