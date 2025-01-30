package org.example;

import org.springframework.stereotype.Service;
import org.springframework.kafka.annotation.KafkaListener;

@Service
public class KafkaConsumer {
    @KafkaListener(topics = "test", groupId = "new_test")
    public void listen(String message){
        System.out.println("Received message: " + message);
    }

}
