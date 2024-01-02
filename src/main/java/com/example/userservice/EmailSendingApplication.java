package com.example.userservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class EmailSendingApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmailSendingApplication.class, args);
	}

}
