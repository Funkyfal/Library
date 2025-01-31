package org.example.controllers;

import org.example.entities.Shelf;
import org.example.services.ShelfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ShelfController {
    @Autowired
    private ShelfService shelfService;

    @GetMapping("/shelf/available")
    public ResponseEntity<List<Shelf>> allAvailableBooks(){
        return new ResponseEntity<>(shelfService.getAllAvailableBooks(), HttpStatus.OK);
    }

    @PutMapping("/shelf/take/{book_id}")
    public ResponseEntity<Shelf> takeBook(@PathVariable("book_id") Long book_id) {
        try {
            return new ResponseEntity<>(shelfService.takeBook(book_id), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/shelf/return/{book_id}")
    public ResponseEntity<Shelf> returnBook(@PathVariable("book_id") Long book_id) {
        try {
            return new ResponseEntity<>(shelfService.returnBook(book_id), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
