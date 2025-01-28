import org.example.Repositories.BookRepository;
import org.example.entities.Book;
import org.example.services.BookService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@MockitoSettings(strictness = Strictness.LENIENT)
@ExtendWith(MockitoExtension.class)
public class BookServiceTest {
    @Mock
    BookRepository bookRepository;

    @InjectMocks
    BookService bookService;

    @Test
    void getAllBooks_shouldReturnListOfBooks() {
        Book book1 = new Book();
        book1.setName("Book 1");

        Book book2 = new Book();
        book2.setName("Book 2");

        when(bookRepository.findAll()).thenReturn(List.of(book1, book2));
        List<Book> books = bookService.getAllBooks();

        assertNotNull(books);
        assertEquals(2, books.size());
        verify(bookRepository, times(1)).findAll();
    }

    @Test
    void getBook_shouldReturnBookById() {
        Book book = new Book();
        book.setId(1L);
        book.setName("Test Book");

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        Optional<Book> result = bookService.getBook(1L);

        assertTrue(result.isPresent());
        assertEquals("Test Book", result.get().getName());
        verify(bookRepository, times(1)).findById(1L);
    }

    @Test
    void getBook_shouldReturnEmptyWhenNotFound() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());
        Optional<Book> result = bookService.getBook(1L);

        assertFalse(result.isPresent());
        verify(bookRepository, times(1)).findById(1L);
    }

    @Test
    void getBookByISBN_shouldReturnBookWhenFound() {
        Book book = new Book();
        book.setISBN("123-456");
        book.setName("Test Book");

        when(bookRepository.findByISBN("123-456")).thenReturn(Optional.of(book));
        Optional<Book> result = bookService.getBookByISBN("123-456");

        assertTrue(result.isPresent());
        assertEquals("Test Book", result.get().getName());
        verify(bookRepository, times(1)).findByISBN("123-456");
    }

    @Test
    void getBookByISBN_shouldReturnEmptyWhenNotFound() {
        when(bookRepository.findByISBN("123-456")).thenReturn(Optional.empty());
        Optional<Book> result = bookService.getBookByISBN("123-456");

        assertFalse(result.isPresent());
        verify(bookRepository, times(1)).findByISBN("123-456");
    }

    @Test
    void addNewBook_shouldSaveWhenNotExists() {
        Book book = new Book();
        book.setISBN("123-456");

        when(bookRepository.findByISBN("123-456")).thenReturn(Optional.empty());
        when(bookRepository.save(book)).thenReturn(book);
        Book result = bookService.addNewBook(book);

        assertNotNull(result);
        verify(bookRepository, times(1)).findByISBN("123-456");
        verify(bookRepository, times(1)).save(book);
    }

    @Test
    void addNewBook_shouldReturnExistingWhenExists() {
        Book book = new Book();
        book.setISBN("123-456");
        book.setName("Existing Book");

        when(bookRepository.findByISBN("123-456")).thenReturn(Optional.of(book));
        Book result = bookService.addNewBook(book);

        assertEquals("Existing Book", result.getName());
        verify(bookRepository, times(1)).findByISBN("123-456");
        verify(bookRepository, times(0)).save(book);
    }

    @Test
    void changeBook_shouldUpdateExistingBook() {
        Book existingBook = new Book();
        existingBook.setId(1L);
        existingBook.setName("Old Book");

        Book updatedBook = new Book();
        updatedBook.setName("New Book");

        when(bookRepository.findById(1L)).thenReturn(Optional.of(existingBook));
        when(bookRepository.save(existingBook)).thenReturn(existingBook);
        Book result = bookService.changeBook(1L, updatedBook);

        assertEquals("New Book", result.getName());
        verify(bookRepository, times(1)).findById(1L);
        verify(bookRepository, times(1)).save(existingBook);
    }

    @Test
    void changeBook_shouldThrowExceptionWhenNotFound() {
        Book updatedBook = new Book();
        updatedBook.setName("New Book");

        when(bookRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> bookService.changeBook(1L, updatedBook));
        verify(bookRepository, times(1)).findById(1L);
        verify(bookRepository, times(0)).save(any());
    }

    @Test
    void deleteBook_shouldDeleteWhenExists() {
        Book book = new Book();
        book.setId(1L);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        doNothing().when(bookRepository).deleteById(1L);
        bookService.deleteBook(1L);

        verify(bookRepository, times(1)).deleteById(1L);
    }
}
