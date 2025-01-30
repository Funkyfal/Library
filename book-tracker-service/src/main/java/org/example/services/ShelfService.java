package org.example.services;

import jakarta.transaction.Transactional;
import org.example.entities.Shelf;
import org.example.repositories.ShelfRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShelfService {
    @Autowired
    private ShelfRepository shelfRepository;

    public void addBookToShelf(Long book_id){
        Shelf shelf = new Shelf();
        shelf.setBookId(book_id);
        shelf.setAvailability(true);
        shelf.setTakenAt(null);
        shelf.setReturnBy(null);
        shelfRepository.save(shelf);
    }

    @Transactional
    public void removeBookFromShelf(Long book_id){
        shelfRepository.deleteByBookId(book_id);
    }
}