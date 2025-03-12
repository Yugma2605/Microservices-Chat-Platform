package com.example.messaging_service.user;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Generated;

import java.lang.annotation.Documented;

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
