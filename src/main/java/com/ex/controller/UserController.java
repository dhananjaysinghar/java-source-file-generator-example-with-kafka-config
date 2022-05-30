package com.ex.controller;

import com.ex.kafka.KafkaData;
import com.ex.kafka.generated.producer.KafkaDataSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

   @Autowired
   private KafkaDataSender kafkaDataSender;

    @PostMapping
    public String sendData(@RequestBody KafkaData data) {
        return kafkaDataSender.send(data);
    }
}
