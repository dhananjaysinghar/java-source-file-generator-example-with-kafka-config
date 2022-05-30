package com.ex.kafka.generated.listener.processor;
import org.springframework.stereotype.Service;

@Service
public interface KafkaDataProcessor {
    Object process(com.ex.kafka.KafkaData data);
}