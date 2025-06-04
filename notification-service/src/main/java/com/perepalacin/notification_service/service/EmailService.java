package com.perepalacin.notification_service.service;

import jakarta.annotation.PostConstruct;
import kong.unirest.core.HttpResponse;
import kong.unirest.core.JsonNode;
import kong.unirest.core.Unirest;
import kong.unirest.core.UnirestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Value("${mailgun.api.key}")
    private String mailgunApiKeyValue;

    private static String STATIC_MAILGUN_API_KEY;

    @PostConstruct
    public void init() {
        STATIC_MAILGUN_API_KEY = mailgunApiKeyValue;
    }
        public static JsonNode sendHtmlMessage() throws UnirestException {
            String htmlContent = """
            <html>
            <head>
                <style>
                    body { font-family: Arial, sans-serif; background-color: #f4f4f4; margin: 0; padding: 0; }
                    .container { width: 100%; max-width: 600px; margin: 0 auto; background-color: #ffffff; padding: 20px; border-radius: 8px; box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1); }
                    .header { background-color: #007bff; color: white; padding: 10px 20px; text-align: center; border-radius: 8px 8px 0 0; }
                    .content { padding: 20px; line-height: 1.6; color: #333333; }
                    .footer { text-align: center; padding: 10px; font-size: 0.8em; color: #777777; }
                    h1 { color: #007bff; }
                    p { margin-bottom: 1em; }
                    .button { display: inline-block; padding: 10px 20px; margin-top: 15px; background-color: #28a745; color: white; text-decoration: none; border-radius: 5px; }
                </style>
            </head>
            <body>
                <div class='container'>
                    <div class='header'>
                        <h2>Your Awesome Order Confirmation</h2>
                    </div>
                    <div class='content'>
                        <p>Dear <strong>Juan Dank</strong>,</p>
                        <p>Congratulations! Your order <strong>#12345</strong> has been successfully placed.</p>
                        <p>You are truly awesome! We appreciate your business.</p>
                        <p><strong>Order Details:</strong></p>
                        <ul>
                            <li>Product A: 2 units</li>
                            <li>Product B: 1 unit</li>
                        </ul>
                        <p>Estimated delivery date: <strong>June 7, 2025</strong></p>
                        <p style='text-align: center;'>
                            <a href='https://yourwebsite.com/order/12345' class='button'>View Your Order</a>
                        </p>
                    </div>
                    <div class='footer'>
                        <p>&copy; 2025 Your Company. All rights reserved.</p>
                        <p><a href='#' style='color: #007bff;'>Unsubscribe</a> | <a href='#' style='color: #007bff;'>Privacy Policy</a></p>
                    </div>
                </div>
            </body>
            </html>
            """;

            return request.getBody();
        }

//    @KafkaListener(topics = "purchases", groupId = "purchase-service")
//    public void orderEvent(BookingEvent bookingEvent) {
//        log.info("Received order event: {}", bookingEvent);
//        // Create Order object for DB
//        Order order = createOrder(bookingEvent);
//        orderRepository.saveAndFlush(order);
//
//        // Update Inventory
//        inventoryServiceClient.updateInventory(order.getEventId(), order.getTicketCount());
//        log.info("Inventory updated for event: {}, less tickets: {}", order.getEventId(), order.getTicketCount());
//    }

}