package com.swarnim.userservice.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;

@Configuration
public class KafkaProducerConfig {
    private KafkaTemplate<String, String> kafkaTemplate;

    public KafkaProducerConfig(KafkaTemplate kafkaTemplate){
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishEvent(String topic, String message) {
        kafkaTemplate.send(topic, message);
    }
}
