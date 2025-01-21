package com.example.notification_service;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@EnableEurekaServer
public class NotificationServiceApplication {
    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.load();

        // You can fetch variables from the .env file like this:
        String dbUrl = dotenv.get("DB_URL");
        String dbUsername = dotenv.get("DB_USERNAME");
        String dbPassword = dotenv.get("DB_PASSWORD");
        String mail_username = dotenv.get("MAIL_USERNAME");
        String mail_password = dotenv.get("MAIL_PASSWORD");
        // Use these variables to configure your DataSource or application logic
        System.setProperty("DB_URL", dbUrl);
        System.setProperty("DB_USERNAME", dbUsername);
        System.setProperty("DB_PASSWORD", dbPassword);
        System.setProperty("MAIL_USERNAME", mail_username);
        System.setProperty("MAIL_PASSWORD", mail_password);
        SpringApplication.run(NotificationServiceApplication.class, args);
    }
}