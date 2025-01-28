package org.example.controllers;

import org.example.entities.Book;
import org.example.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class BookController {
    @Autowired
    private BookService bookService;

    @GetMapping("/book")
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    @GetMapping("/book/{id}")
    public Optional<Book> getBook(@PathVariable("id") Long id) {
        return bookService.getBook(id);
    }

    @PostMapping("/book")
    public Book addNewBook(@RequestBody Book book) {
        return bookService.addNewBook(book);
    }

    @PutMapping("/book/{id}")
    public Book changeBook(@PathVariable("id") Long id, @RequestBody Book book) {
        return bookService.changeBook(id, book);
    }

    @DeleteMapping("/book/{id}")
    public void deleteBook(@PathVariable("id") Long id){
        bookService.deleteBook(id);
    }
}
