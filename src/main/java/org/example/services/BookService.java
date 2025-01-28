package org.example.services;

import org.example.Repositories.BookRepository;
import org.example.entities.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository;

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Optional<Book> getBook(Long id) {
        return bookRepository.findById(id);
    }

    public Book addNewBook(Book book) {
        return bookRepository.save(book);
    }

    public Book changeBook(Long id, Book book) {
        return bookRepository.findById(id).map(existingBook ->
        {
            existingBook.setAuthor(book.getAuthor());
            existingBook.setDescription(book.getDescription());
            existingBook.setName(book.getName());
            existingBook.setGenre(book.getGenre());
            existingBook.setISBN(book.getISBN());
            return bookRepository.save(existingBook);
        }
        ).orElseThrow(() -> new IllegalArgumentException("Book with id " + " not found"));
    }

    public void deleteBook(Long id){
        bookRepository.deleteById(id);
    }
}
