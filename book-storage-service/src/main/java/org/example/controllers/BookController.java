package org.example.controllers;

import org.example.entities.Book;
import org.example.kafka.KafkaProducer;
import org.example.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class BookController {
    @Autowired
    private BookService bookService;
    @Autowired
    private KafkaProducer kafkaProducer;

    @GetMapping("/book")
    public ResponseEntity<List<Book>> getAllBooks() {
        return new ResponseEntity<>(bookService.getAllBooks(), HttpStatus.OK);
    }

    @GetMapping("/book/{id}")
    public ResponseEntity<Book> getBook(@PathVariable("id") Long id) {
        return bookService.getBook(id)
                .map(book -> new ResponseEntity<>(book, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/book/isbn/{isbn}")
    public ResponseEntity<Book> getBookByISBN(@PathVariable("isbn") String ISBN) {
        return bookService.getBookByISBN(ISBN)
                .map(book -> new ResponseEntity<>(book, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/book")
    public ResponseEntity<Book> addNewBook(@RequestBody Book book) {
        Book createdBook = bookService.addNewBook(book);
        kafkaProducer.sendBookAction("add", createdBook.getId().toString());
        return new ResponseEntity<>(createdBook, HttpStatus.CREATED);
    }

    @PutMapping("/book/{id}")
    public ResponseEntity<Book> changeBook(@PathVariable("id") Long id, @RequestBody Book book) {
        try {
            return new ResponseEntity<>(bookService.changeBook(id, book), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/book/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable("id") Long id) {
        if (bookService.getBook(id).isPresent()) {
            bookService.deleteBook(id);
            kafkaProducer.sendBookAction("delete", id.toString());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
