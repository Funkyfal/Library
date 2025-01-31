package org.example.services;

import jakarta.transaction.Transactional;
import org.example.entities.Shelf;
import org.example.repositories.ShelfRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ShelfService {
    @Autowired
    private ShelfRepository shelfRepository;

    public void addBookToShelf(Long book_id){
        shelfRepository.findByBookId(book_id)
                .orElseGet(() -> {
                    Shelf shelf = new Shelf();
                    shelf.setBookId(book_id);
                    shelf.setAvailability(true);
                    shelf.setTakenAt(null);
                    shelf.setReturnBy(null);
                    shelfRepository.save(shelf);
                    return shelf;
                });
    }

    @Transactional
    public void removeBookFromShelf(Long book_id){
        shelfRepository.deleteByBookId(book_id);
    }

    public Shelf takeBook(Long book_id){
        Shelf shelf = shelfRepository.findByBookId(book_id)
                .orElseThrow(() -> new RuntimeException("No such available book here."));
        if(!shelf.isAvailable())
            throw new RuntimeException("You can't take this book");

        shelf.setAvailability(false);
        shelf.setTakenAt(LocalDateTime.now());
        shelf.setReturnBy(LocalDateTime.now().plusMonths(1));
        return shelfRepository.save(shelf);
    }

    public Shelf returnBook(Long book_id){
        Shelf shelf = shelfRepository.findByBookId(book_id)
                .orElseThrow(() -> new RuntimeException("No such available book here."));
        if(shelf.isAvailable())
            throw new RuntimeException("You can't return this book");
        if(LocalDateTime.now().isAfter(shelf.getReturnBy()))
            System.out.println("You have returned your book too late! :)");

        shelf.setAvailability(true);
        shelf.setReturnBy(null);
        shelf.setTakenAt(null);
        return shelfRepository.save(shelf);
    }

    public List<Shelf> getAllAvailableBooks(){
        return shelfRepository.findAllByAvailability(true);
    }
}