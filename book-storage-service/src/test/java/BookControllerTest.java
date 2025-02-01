import org.example.controllers.BookController;
import org.example.entities.Book;
import org.example.kafka.BookKafkaProducer;
import org.example.services.BookService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookControllerTest {

    @Mock
    private BookService bookService;

    @Mock
    private BookKafkaProducer bookKafkaProducer;

    @InjectMocks
    private BookController bookController;

    @Test
    void getAllBooks_shouldReturnListOfBooks() {
        Book book1 = new Book();
        book1.setName("Book 1");

        Book book2 = new Book();
        book2.setName("Book 2");

        when(bookService.getAllBooks()).thenReturn(Arrays.asList(book1, book2));
        ResponseEntity<List<Book>> response = bookController.getAllBooks();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        verify(bookService, times(1)).getAllBooks();
    }

    @Test
    void getBook_shouldReturnBookWhenFound() {
        Book book = new Book();
        book.setId(1L);
        book.setName("Test Book");

        when(bookService.getBook(1L)).thenReturn(Optional.of(book));
        ResponseEntity<Book> response = bookController.getBook(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Test Book", response.getBody().getName());
        verify(bookService, times(1)).getBook(1L);
    }

    @Test
    void getBook_shouldReturnNotFoundWhenMissing() {
        when(bookService.getBook(1L)).thenReturn(Optional.empty());
        ResponseEntity<Book> response = bookController.getBook(1L);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(bookService, times(1)).getBook(1L);
    }

    @Test
    void getBookByISBN_shouldReturnBookWhenFound() {
        Book book = new Book();
        book.setISBN("123-456");
        book.setName("Test Book");

        when(bookService.getBookByISBN("123-456")).thenReturn(Optional.of(book));
        ResponseEntity<Book> response = bookController.getBookByISBN("123-456");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Test Book", response.getBody().getName());
        verify(bookService, times(1)).getBookByISBN("123-456");
    }

    @Test
    void addNewBook_shouldReturnCreatedBook() {
        Book book = new Book();
        book.setISBN("123-456");
        book.setId(1L); // Устанавливаем идентификатор

        when(bookService.addNewBook(book)).thenReturn(book);
        ResponseEntity<Book> response = bookController.addNewBook(book);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(bookService, times(1)).addNewBook(book);
        verify(bookKafkaProducer, times(1)).sendBookAction("add", book.getId().toString());
    }


    @Test
    void changeBook_shouldReturnUpdatedBook() {
        Book updatedBook = new Book();
        updatedBook.setName("New Book");

        Book existingBook = new Book();
        existingBook.setId(1L);
        existingBook.setName("Old Book");

        when(bookService.changeBook(1L, updatedBook)).thenReturn(existingBook);
        ResponseEntity<Book> response = bookController.changeBook(1L, updatedBook);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(bookService, times(1)).changeBook(1L, updatedBook);
    }

    @Test
    void deleteBook_shouldReturnNoContentWhenBookExists() {
        Book book = new Book();
        book.setId(1L);

        when(bookService.getBook(1L)).thenReturn(Optional.of(book));
        doNothing().when(bookService).deleteBook(1L);
        ResponseEntity<Void> response = bookController.deleteBook(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(bookService, times(1)).deleteBook(1L);
        verify(bookKafkaProducer, times(1)).sendBookAction("delete", "1");
    }

    @Test
    void deleteBook_shouldReturnNotFoundWhenBookMissing() {
        when(bookService.getBook(1L)).thenReturn(Optional.empty());
        ResponseEntity<Void> response = bookController.deleteBook(1L);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(bookService, never()).deleteBook(1L);
        verify(bookKafkaProducer, never()).sendBookAction(anyString(), anyString());
    }
}
