package org.example.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ShelfKafkaConfiguration {
    @Bean
    public NewTopic newTopic() {
        return new NewTopic("book-validate", 1, (short) 1);
    }
}
