package com.example.upload_service.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "secretKeys")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SecretKeys {

    @Id
    @Column(name = "keyName") // Maps the 'accessKey' column
    private String keyName;

    @Column(name = "keyValue") // Maps the 'secretKeyValue' column
    private String keyValue;
}
