package com.example.upload_service.entity;

import jakarta.persistence.*;
import lombok.Data;
//import org.springframework.data.annotation.Id;
import jakarta.persistence.Id;

import java.util.Date;

@Entity
@Table(name = "file_metadata")
@Data
public class FileMetaData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "username")
    private String username;

    @Column(name = "file_size")
    private Long fileSize;

    @Column(name = "file_type")
    private String fileType;

    @Column(name = "upload_date")
    private Date uploadDate;

}
