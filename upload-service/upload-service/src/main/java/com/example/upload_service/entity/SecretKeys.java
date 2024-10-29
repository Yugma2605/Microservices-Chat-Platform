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

    @Id  // Use @Id on the column you want to act as a unique identifier.
    @Column(name = "key")  // This should match the actual column name in the table.
    private String keyName;  // Rename field as per Java conventions, e.g., keyName.

    @Column(name = "value")  // This should match the actual column name in the table.
    private String keyValue;

}
