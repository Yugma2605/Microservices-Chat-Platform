package com.example.auth_service.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "auth_metadata")
@Data
public class AuthEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "firstName")
    private String firstName;

    @Column(name = "password")
    private String password;

    @Column(name = "lastName")
    private String lastName;
}
