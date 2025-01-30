package org.example;

import org.example.services.ShelfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.kafka.annotation.KafkaListener;

@Service
public class KafkaConsumer {
    @Autowired
    private ShelfService shelfService;

    @KafkaListener(topics = "book-action", groupId = "new_book")
    public void consumeBookId(String message){
        String action = message.split(":")[0];
        Long id = Long.parseLong(message.split(":")[1]);
        if ("add".equals(action)) {
            shelfService.addBookToShelf(id);
            System.out.println("Added book with ID " + id + " to shelf");
        } else if ("delete".equals(action)) {
            shelfService.removeBookFromShelf(id);
            System.out.println("Removed book with ID " + id + " from shelf");
        }
    }

}