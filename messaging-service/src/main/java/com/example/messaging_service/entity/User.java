package com.example.messaging_service.entity;

import com.example.messaging_service.user.Status;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "userTable")
public class User {
    @Id
    private String userId;
    private String fullName;
    private Status status;
    private String email;
}
