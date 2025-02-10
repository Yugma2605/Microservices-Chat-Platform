package com.example.api_gateway.repository;

import com.example.api_gateway.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface UserRepository extends JpaRepository<Users, Long> {
    Users findByUsername(String username);
}