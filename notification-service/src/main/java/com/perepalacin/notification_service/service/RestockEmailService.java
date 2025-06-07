package com.perepalacin.notification_service.service;

import com.perepalacin.notification_service.entity.dto.RestockEmailData;
import jakarta.annotation.PostConstruct;
import kong.unirest.core.HttpResponse;
import kong.unirest.core.JsonNode;
import kong.unirest.core.Unirest;
import kong.unirest.core.UnirestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class RestockEmailService {

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


    @KafkaListener(topics = "inventory-events", groupId = "restock-events")
    public static JsonNode handleItemRestock(final RestockEmailData restockEmailData) throws UnirestException {
        StringBuilder productListHtml = new StringBuilder();
        productListHtml.append(String.format("<li>%s</li>", restockEmailData.getProductDto().getName()));

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
                        <h2>The item you are looking for is back in stock!</h2>
                    </div>
                    <div class='content'>
                        <p>Dear <strong>customer</strong>,</p>
                        <p>The product <strong>%s</strong> is back in stock.</p>
                        <p>You are truly awesome! We appreciate your business.</p>
                        <ul>
                            %s
                        </ul>
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
                restockEmailData.getProductDto(),
                productListHtml
        );

        HttpResponse<JsonNode> request = Unirest.post("https://api.mailgun.net/v3/" + STATIC_MAILGUN_DOMAIN + "/messages")
                .basicAuth("api", STATIC_MAILGUN_API_KEY)
                .queryString("from", "Mailgun Sandbox <postmaster@" + STATIC_MAILGUN_DOMAIN + ">")
                .queryString("to", String.join(",", restockEmailData.getEmails()))
                .queryString("subject", "Your Order has been modified - Awesome!")
                .queryString("html", finalHtmlContent)
                .asJson();
        return request.getBody();
    }
}
