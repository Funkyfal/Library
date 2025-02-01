import org.example.controllers.ShelfController;
import org.example.entities.Shelf;
import org.example.kafka.ShelfKafkaProducer;
import org.example.services.ShelfService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ShelfControllerTest {

    @Mock
    private ShelfService shelfService;

    @Mock
    private ShelfKafkaProducer shelfKafkaProducer;

    @InjectMocks
    private ShelfController shelfController;

    @Test
    public void testAllAvailableBooks() {
        Shelf shelf1 = new Shelf();
        shelf1.setId(1L);
        shelf1.setBookId(100L);
        shelf1.setAvailability(true);

        Shelf shelf2 = new Shelf();
        shelf2.setId(2L);
        shelf2.setBookId(101L);
        shelf2.setAvailability(true);

        List<Shelf> availableShelves = Arrays.asList(shelf1, shelf2);
        when(shelfService.getAllAvailableBooks()).thenReturn(availableShelves);

        ResponseEntity<List<Shelf>> response = shelfController.allAvailableBooks();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        verify(shelfService, times(1)).getAllAvailableBooks();
    }

    @Test
    public void testAddBookToShelf() {
        Long bookId = 123L;
        shelfController.addBookToShelf(bookId);
        verify(shelfKafkaProducer, times(1)).sendShelfAction(bookId.toString());
    }

    @Test
    public void testTakeBookSuccess() {
        Long bookId = 200L;
        Shelf shelf = new Shelf();
        shelf.setId(1L);
        shelf.setBookId(bookId);
        shelf.setAvailability(false);
        shelf.setTakenAt(LocalDateTime.now());
        shelf.setReturnBy(LocalDateTime.now().plusMonths(1));

        when(shelfService.takeBook(bookId)).thenReturn(shelf);

        ResponseEntity<Shelf> response = shelfController.takeBook(bookId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isAvailable());
        verify(shelfService, times(1)).takeBook(bookId);
    }

    @Test
    public void testTakeBookNotFound() {
        Long bookId = 300L;
        when(shelfService.takeBook(bookId)).thenThrow(new RuntimeException("No such available book here."));
        ResponseEntity<Shelf> response = shelfController.takeBook(bookId);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(shelfService, times(1)).takeBook(bookId);
    }

    @Test
    public void testReturnBookSuccess() {
        Long bookId = 400L;
        Shelf shelf = new Shelf();
        shelf.setId(2L);
        shelf.setBookId(bookId);
        shelf.setAvailability(true);
        shelf.setTakenAt(null);
        shelf.setReturnBy(null);

        when(shelfService.returnBook(bookId)).thenReturn(shelf);

        ResponseEntity<Shelf> response = shelfController.returnBook(bookId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isAvailable());
        verify(shelfService, times(1)).returnBook(bookId);
    }

    @Test
    public void testReturnBookNotFound() {
        Long bookId = 500L;
        when(shelfService.returnBook(bookId)).thenThrow(new RuntimeException("No such available book here."));
        ResponseEntity<Shelf> response = shelfController.returnBook(bookId);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(shelfService, times(1)).returnBook(bookId);
    }

    @Test
    public void testDeleteBookFromShelf() {
        Long bookId = 600L;
        shelfController.deleteBookFromShelf(bookId);
        verify(shelfService, times(1)).removeBookFromShelf(bookId);
    }
}
