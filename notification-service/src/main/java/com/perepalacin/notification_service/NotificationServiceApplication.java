package com.perepalacin.notification_service;

import com.perepalacin.notification_service.service.EmailService;
import kong.unirest.core.JsonNode;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class NotificationServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(NotificationServiceApplication.class, args);
		try {
			JsonNode response = EmailService.sendHtmlMessage();
			System.out.println("Mailgun API Response: " + response.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
