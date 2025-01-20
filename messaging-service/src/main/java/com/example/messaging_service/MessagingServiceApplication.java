package com.example.messaging_service;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;
import io.github.cdimascio.dotenv.Dotenv;


@SpringBootApplication
@EnableEurekaServer
public class MessagingServiceApplication {

	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.load();  // Loads .env file

		// You can fetch variables from the .env file like this:
		String dbUrl = dotenv.get("DB_URL");
		String dbUsername = dotenv.get("DB_USERNAME");
		String dbPassword = dotenv.get("DB_PASSWORD");

		// Use these variables to configure your DataSource or application logic
		System.setProperty("DB_URL", dbUrl);
		System.setProperty("DB_USERNAME", dbUsername);
		System.setProperty("DB_PASSWORD", dbPassword);
		SpringApplication.run(MessagingServiceApplication.class, args);
	}


	@Bean
	public WebClient webClient() {
		return WebClient.builder().build();
	}
}
