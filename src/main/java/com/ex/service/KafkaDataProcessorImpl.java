package com.ex.service;

import com.ex.kafka.KafkaData;
import com.ex.kafka.generated.listener.processor.KafkaDataProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class KafkaDataProcessorImpl implements KafkaDataProcessor {

    @Override
    public KafkaData process(KafkaData data) {
        log.info("Processing data='{}'", data);
        return data;
    }
}
