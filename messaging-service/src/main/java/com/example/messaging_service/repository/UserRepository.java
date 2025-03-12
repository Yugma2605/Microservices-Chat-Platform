package com.example.messaging_service.repository;

import com.example.messaging_service.user.Status;
import com.example.messaging_service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    List<User> findAllByStatus(Status status);
}
