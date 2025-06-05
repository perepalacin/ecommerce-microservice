package com.perepalacin.notification_service.service;

import com.perepalacin.notification_service.entity.dto.PurchaseEmailData;
import com.perepalacin.notification_service.entity.dto.RestockEmailData;
import kong.unirest.core.JsonNode;
import kong.unirest.core.UnirestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RestockEmailService {

    @KafkaListener(topics = "inventory-events", groupId = "restock-events")
    public static JsonNode handleItemRestock(final RestockEmailData restockEmailData) throws UnirestException {
        return null;
    }
}
