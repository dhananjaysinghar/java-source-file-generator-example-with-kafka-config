package com.ex.kafka.generated.config;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.retry.annotation.EnableRetry;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
@EnableRetry
@ConditionalOnProperty(prefix = "app", name = "kafka.enabled", havingValue = "true")
public class KafkaConsumerConfig {

    @Value("${app.kafka.host}")
    private String kafkaHost;

    @Value("${app.kafka.max-poll-record:1000}")
    private int maxPollRecord;

    @Value("${app.kafka.consumer-group-id:user-consumer-test}")
    private String kafkaConsumerGroupId;

    private static final Map<String, Object> DEFAULT_CONSUMER_CONFIG_MAP = new HashMap<>();

    @PostConstruct
    public void configureKafkaProperties() {
        DEFAULT_CONSUMER_CONFIG_MAP.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaHost);
        DEFAULT_CONSUMER_CONFIG_MAP.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        DEFAULT_CONSUMER_CONFIG_MAP.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        DEFAULT_CONSUMER_CONFIG_MAP.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, maxPollRecord);
    }

    @Bean
    public ConsumerFactory<String, com.ex.kafka.KafkaData> userConsumerFactory() {
        Map<String, Object> map = new HashMap<>(DEFAULT_CONSUMER_CONFIG_MAP);
        map.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaConsumerGroupId);
        JsonDeserializer<com.ex.kafka.KafkaData> userJsonDeserializer = new JsonDeserializer<>(com.ex.kafka.KafkaData.class);
        userJsonDeserializer.ignoreTypeHeaders();
        return new DefaultKafkaConsumerFactory<>(map, new StringDeserializer(), userJsonDeserializer);

    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, com.ex.kafka.KafkaData> concurrentKafkaListenerUserConsumerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, com.ex.kafka.KafkaData> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(userConsumerFactory());
        return factory;
    }}