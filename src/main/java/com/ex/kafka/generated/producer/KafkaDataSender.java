package com.ex.kafka.generated.producer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
public class KafkaDataSender {

    @Autowired
    private KafkaTemplate<String, com.ex.kafka.KafkaData> kafkaTemplate;

    @Value("${app.kafka.topic:test_topic}")
    private String topicName;

    @Value("${app.kafka.enabled:false}")
    private boolean isKafkaEnabled;


    public String send(com.ex.kafka.KafkaData data) {
        log.info("sending data='{}={}' to topic='{}'", data.hashCode(), data, topicName);
        String key = UUID.randomUUID().toString();
        if (isKafkaEnabled) {
            kafkaTemplate.send(topicName, key, data);
            return "data sent successfully";
        }
        return "kafka is disabled in application";
    }
}