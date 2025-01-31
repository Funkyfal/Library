package org.example.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BookKafkaConfiguration {
    @Bean
    public NewTopic newTopic(){
        return new NewTopic("book-action", 1, (short) 1);
    }

    @Bean
    public NewTopic bookValidationResponseTopic(){
        return new NewTopic("book-validate-response", 1, (short) 1);
    }
}
