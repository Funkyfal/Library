package org.example.kafka;

import org.example.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class BookKafkaConsumer {
    @Autowired
    private BookService bookService;
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @KafkaListener(topics = "book-validate", groupId = "book-storage")
    public void bookValidateResponse(String book_id){
        boolean result = bookService.getBook(Long.parseLong(book_id)).isPresent();
        kafkaTemplate.send("book-validate-response", book_id + ":" + result);
    }
}
