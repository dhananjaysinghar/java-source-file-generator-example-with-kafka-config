package com.ex.kafka.generated.listener;
import lombok.extern.slf4j.Slf4j;
import com.ex.kafka.generated.listener.processor.KafkaDataProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class KafkaDataListener {

    @Autowired
    private KafkaDataProcessor kafkaDataProcessor;


    @KafkaListener(topics = "${app.kafka.topic}", groupId = "${app.kafka.consumer-group-id}", containerFactory = "concurrentKafkaListenerUserConsumerFactory")
    public void receive(com.ex.kafka.KafkaData data) {
        log.info("Received data in kafka listener : {}", data);
        kafkaDataProcessor.process(data);
    }
}