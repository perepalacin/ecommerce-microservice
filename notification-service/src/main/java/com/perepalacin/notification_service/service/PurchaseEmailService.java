package com.perepalacin.notification_service.service;

import com.perepalacin.notification_service.entity.dto.PurchaseEmailData;
import com.perepalacin.notification_service.entity.dto.PurchaseItemDto;
import jakarta.annotation.PostConstruct;
import kong.unirest.core.HttpResponse;
import kong.unirest.core.JsonNode;
import kong.unirest.core.Unirest;
import kong.unirest.core.UnirestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;

@Service
@Slf4j
public class PurchaseEmailService {

    @Value("${mailgun.api.key}")
    private String mailgunApiKeyValue;
    @Value("${mailgun.domain}")
    private String mailgunDomain;

    private static String STATIC_MAILGUN_API_KEY;
    private static String STATIC_MAILGUN_DOMAIN;

    @PostConstruct
    public void init() {
        STATIC_MAILGUN_API_KEY = mailgunApiKeyValue;
        STATIC_MAILGUN_DOMAIN = mailgunDomain;
    }

    // %s for string, %d for integer, %.2f for BigDecimal with 2 decimal places


    public static JsonNode sendPurchaseConfirmationEmail(final PurchaseEmailData purchaseEmailData) throws UnirestException {
        StringBuilder productListHtml = new StringBuilder();
        for (PurchaseItemDto product : purchaseEmailData.getPurchaseItems()) {
            productListHtml.append(String.format("<li>%s: %d units</li>", product.getName(), product.getQuantity()));
        }

        String formattedDeliveryDate = purchaseEmailData.getDeliveryDate().format(DateTimeFormatter.ofPattern("MMMM d, yyyy"));

        String htmlContentTemplate = """
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
                        <p>Dear <strong>%s</strong>,</p>
                        <p>Congratulations! Your order <strong>#%s</strong> has been successfully placed.</p>
                        <p>Your total price was: <strong>$%.2f</strong>.</p>
                        <p>You are truly awesome! We appreciate your business.</p>
                        <p><strong>Order Details:</strong></p>
                        <ul>
                            %s
                        </ul>
                        <p>Estimated delivery date: <strong>%s</strong></p>
                        <p style='text-align: center;'>
                            <a href='https://yourwebsite.com/order/%s' class='button'>View Your Order</a>
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

        String finalHtmlContent = String.format(htmlContentTemplate,
                purchaseEmailData.getRecipientFullName(),
                purchaseEmailData.getPurchaseId(),
                purchaseEmailData.getTotalPrice(),
                productListHtml,
                formattedDeliveryDate,
                purchaseEmailData.getPurchaseId()
        );

        HttpResponse<JsonNode> request = Unirest.post("https://api.mailgun.net/v3/" + STATIC_MAILGUN_DOMAIN + "/messages")
                .basicAuth("api", STATIC_MAILGUN_API_KEY)
                .queryString("from", "Mailgun Sandbox <postmaster@" + STATIC_MAILGUN_DOMAIN + ">")
                .queryString("to", purchaseEmailData.getRecipientFullName() + " <" + purchaseEmailData.getEmail() + ">")
                .queryString("subject", "Your Order Confirmation - Awesome!")
                .queryString("html", finalHtmlContent)
                .asJson();

        return request.getBody();
    }

    public static JsonNode sendPurchaseEditEmail(final PurchaseEmailData purchaseEmailData) throws UnirestException {
        StringBuilder productListHtml = new StringBuilder();
        for (PurchaseItemDto product : purchaseEmailData.getPurchaseItems()) {
            productListHtml.append(String.format("<li>%s: %d units</li>", product.getName(), product.getQuantity()));
        }

        String formattedDeliveryDate = purchaseEmailData.getDeliveryDate().format(DateTimeFormatter.ofPattern("MMMM d, yyyy"));

        String htmlContentTemplate = """
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
                        <h2>Your Order has been modified successfully</h2>
                    </div>
                    <div class='content'>
                        <p>Dear <strong>%s</strong>,</p>
                        <p>Congratulations! Your order <strong>#%s</strong> has been successfully modified.</p>
                        <p>Your total price was: <strong>$%.2f</strong>.</p>
                        <p>You are truly awesome! We appreciate your business.</p>
                        <p><strong>Order Details:</strong></p>
                        <ul>
                            %s
                        </ul>
                        <p>Estimated delivery date: <strong>%s</strong></p>
                        <p style='text-align: center;'>
                            <a href='https://yourwebsite.com/order/%s' class='button'>View Your Order</a>
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

        String finalHtmlContent = String.format(htmlContentTemplate,
                purchaseEmailData.getRecipientFullName(),
                purchaseEmailData.getPurchaseId(),
                purchaseEmailData.getTotalPrice(),
                productListHtml,
                formattedDeliveryDate,
                purchaseEmailData.getPurchaseId()
        );

        HttpResponse<JsonNode> request = Unirest.post("https://api.mailgun.net/v3/" + STATIC_MAILGUN_DOMAIN + "/messages")
                .basicAuth("api", STATIC_MAILGUN_API_KEY)
                .queryString("from", "Mailgun Sandbox <postmaster@" + STATIC_MAILGUN_DOMAIN + ">")
                .queryString("to", purchaseEmailData.getRecipientFullName() + " <" + purchaseEmailData.getEmail() + ">")
                .queryString("subject", "Your Order has been modified - Awesome!")
                .queryString("html", finalHtmlContent)
                .asJson();
        return request.getBody();
    }

    public static JsonNode sendPurchaseDeleteEmail(final PurchaseEmailData purchaseEmailData) throws UnirestException {
        StringBuilder productListHtml = new StringBuilder();
        for (PurchaseItemDto product : purchaseEmailData.getPurchaseItems()) {
            productListHtml.append(String.format("<li>%s: %d units</li>", product.getName(), product.getQuantity()));
        }

        String htmlContentTemplate = """
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
                        <h2>Your Order has been cancelled successfully</h2>
                    </div>
                    <div class='content'>
                        <p>Dear <strong>%s</strong>,</p>
                        <p>Your order <strong>#%s</strong> has been successfully cancelled.</p>
                        <p><strong>Order Details:</strong></p>
                        <ul>
                            %s
                        </ul>
                        <p style='text-align: center;'>
                            <a href='https://yourwebsite.com/order/%s' class='button'>View Your Order</a>
                        </p>
                        <p>Please contact us if you think this was a mistake.</p>
                    </div>
                    <div class='footer'>
                        <p>&copy; 2025 Your Company. All rights reserved.</p>
                        <p><a href='#' style='color: #007bff;'>Unsubscribe</a> | <a href='#' style='color: #007bff;'>Privacy Policy</a></p>
                    </div>
                </div>
            </body>
            </html>
            """;

        String finalHtmlContent = String.format(htmlContentTemplate,
                purchaseEmailData.getRecipientFullName(),
                purchaseEmailData.getPurchaseId(),
                purchaseEmailData.getTotalPrice(),
                productListHtml,
                purchaseEmailData.getPurchaseId()
        );

        HttpResponse<JsonNode> request = Unirest.post("https://api.mailgun.net/v3/" + STATIC_MAILGUN_DOMAIN + "/messages")
                .basicAuth("api", STATIC_MAILGUN_API_KEY)
                .queryString("from", "Mailgun Sandbox <postmaster@" + STATIC_MAILGUN_DOMAIN + ">")
                .queryString("to", purchaseEmailData.getRecipientFullName() + " <" + purchaseEmailData.getEmail() + ">")
                .queryString("subject", "Your Order has been cancelled - Awesome!")
                .queryString("html", finalHtmlContent)
                .asJson();
        return request.getBody();
    }

    @KafkaListener(topics = "purchases", groupId = "purchase-events")
    public static JsonNode handleNewPurchaseEvent(final PurchaseEmailData purchaseEmailData) throws UnirestException {
        if ("new-purchase".equals(purchaseEmailData.getEventType())) {
            return sendPurchaseConfirmationEmail(purchaseEmailData);
        } else if ("edit-purchase".equals(purchaseEmailData.getEventType())) {
            return sendPurchaseEditEmail(purchaseEmailData);
        } else if ("cancel-purchase".equals(purchaseEmailData.getEventType())) {
            return sendPurchaseDeleteEmail(purchaseEmailData);
        }
        log.error("Invalid purchase Event");
        return null;
    }
}