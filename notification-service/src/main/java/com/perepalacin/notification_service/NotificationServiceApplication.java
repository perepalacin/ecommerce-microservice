package com.perepalacin.notification_service;

import com.perepalacin.notification_service.service.PurchaseEmailService;
import kong.unirest.core.JsonNode;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class NotificationServiceApplication {

	public static void main(String[] args) {SpringApplication.run(NotificationServiceApplication.class, args);}

}
