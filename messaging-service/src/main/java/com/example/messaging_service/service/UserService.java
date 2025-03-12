package com.example.messaging_service.service;

import com.example.messaging_service.repository.UserRepository;
import com.example.messaging_service.user.Status;
import com.example.messaging_service.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository repository;
    public void saveUser(User user){
        user.setStatus(Status.ONLINE);
        log.info("User Status : ", user.getFullName() + user.getStatus());
        repository.save(user);
    }
    public void disconnect(User user){
        var storedUser = repository.findById(user.getUserId())
                .orElse(null);

        if(storedUser != null){
            storedUser.setStatus(Status.OFFLINE);
            repository.save(storedUser);
        }

    }
    public Optional<User> getEmailfromId(String userId){
        return repository.findById(userId);
    }
    public Optional<User> getFullnamefromId(String userId){
        return repository.findById(userId);
    }
    public List<User> findConnectedUsers(){
        return repository.findAllByStatus(Status.ONLINE);
    }
}
