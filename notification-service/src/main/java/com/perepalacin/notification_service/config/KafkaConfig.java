package com.perepalacin.notification_service.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
@Profile("production")
public class KafkaConfig {

    @Bean
    public NewTopic purchaseEvents() {
        return TopicBuilder.name("purchase-events")
                .partitions(10)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic restockEmails() {
        return TopicBuilder.name("inventory-events")
                .partitions(10)
                .replicas(1)
                .build();
    }

}
