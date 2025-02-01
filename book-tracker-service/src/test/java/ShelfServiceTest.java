import org.example.entities.Shelf;
import org.example.repositories.ShelfRepository;
import org.example.services.ShelfService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ShelfServiceTest {

    @Mock
    private ShelfRepository shelfRepository;

    @InjectMocks
    private ShelfService shelfService;

    @Test
    void testAddBookToShelf_WhenShelfDoesNotExist() {
        Long bookId = 1L;
        when(shelfRepository.findByBookId(bookId)).thenReturn(Optional.empty());

        Shelf shelf = new Shelf();
        shelf.setId(100L);
        shelf.setBookId(bookId);
        shelf.setAvailability(true);
        when(shelfRepository.save(any(Shelf.class))).thenReturn(shelf);

        shelfService.addBookToShelf(bookId);

        verify(shelfRepository, times(1)).save(any(Shelf.class));
    }

    @Test
    void testAddBookToShelf_WhenShelfAlreadyExists() {
        Long bookId = 1L;
        Shelf existingShelf = new Shelf();
        existingShelf.setId(101L);
        existingShelf.setBookId(bookId);
        existingShelf.setAvailability(true);

        when(shelfRepository.findByBookId(bookId)).thenReturn(Optional.of(existingShelf));

        shelfService.addBookToShelf(bookId);
        verify(shelfRepository, never()).save(any(Shelf.class));
    }

    @Test
    void testRemoveBookFromShelf() {
        Long bookId = 2L;
        shelfService.removeBookFromShelf(bookId);
        verify(shelfRepository, times(1)).deleteByBookId(bookId);
    }

    @Test
    void testTakeBook_Success() {
        Long bookId = 3L;
        Shelf shelf = new Shelf();
        shelf.setId(200L);
        shelf.setBookId(bookId);
        shelf.setAvailability(true);

        when(shelfRepository.findByBookId(bookId)).thenReturn(Optional.of(shelf));
        when(shelfRepository.save(any(Shelf.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Shelf updatedShelf = shelfService.takeBook(bookId);
        assertFalse(updatedShelf.isAvailable());
        assertNotNull(updatedShelf.getTakenAt());
        assertNotNull(updatedShelf.getReturnBy());
        verify(shelfRepository, times(1)).save(shelf);
    }

    @Test
    void testTakeBook_WhenNotAvailable() {
        Long bookId = 3L;
        Shelf shelf = new Shelf();
        shelf.setId(201L);
        shelf.setBookId(bookId);
        shelf.setAvailability(false); // уже занята

        when(shelfRepository.findByBookId(bookId)).thenReturn(Optional.of(shelf));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> shelfService.takeBook(bookId));
        assertEquals("You can't take this book", exception.getMessage());
    }

    @Test
    void testTakeBook_NotFound() {
        Long bookId = 99L;
        when(shelfRepository.findByBookId(bookId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> shelfService.takeBook(bookId));
        assertEquals("No such available book here.", exception.getMessage());
    }

    @Test
    void testReturnBook_Success() {
        Long bookId = 4L;
        Shelf shelf = new Shelf();
        shelf.setId(300L);
        shelf.setBookId(bookId);
        shelf.setAvailability(false);
        shelf.setTakenAt(LocalDateTime.now().minusDays(5));
        shelf.setReturnBy(LocalDateTime.now().plusDays(25));

        when(shelfRepository.findByBookId(bookId)).thenReturn(Optional.of(shelf));
        when(shelfRepository.save(any(Shelf.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Shelf updatedShelf = shelfService.returnBook(bookId);
        assertTrue(updatedShelf.isAvailable());
        assertNull(updatedShelf.getTakenAt());
        assertNull(updatedShelf.getReturnBy());
        verify(shelfRepository, times(1)).save(shelf);
    }

    @Test
    void testReturnBook_WhenAlreadyAvailable() {
        Long bookId = 4L;
        Shelf shelf = new Shelf();
        shelf.setId(301L);
        shelf.setBookId(bookId);
        shelf.setAvailability(true);

        when(shelfRepository.findByBookId(bookId)).thenReturn(Optional.of(shelf));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> shelfService.returnBook(bookId));
        assertEquals("You can't return this book", exception.getMessage());
    }

    @Test
    void testReturnBook_NotFound() {
        Long bookId = 99L;
        when(shelfRepository.findByBookId(bookId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> shelfService.returnBook(bookId));
        assertEquals("No such available book here.", exception.getMessage());
    }

    @Test
    void testGetAllAvailableBooks() {
        Shelf shelf1 = new Shelf();
        shelf1.setId(400L);
        shelf1.setBookId(10L);
        shelf1.setAvailability(true);

        Shelf shelf2 = new Shelf();
        shelf2.setId(401L);
        shelf2.setBookId(11L);
        shelf2.setAvailability(true);

        List<Shelf> availableShelves = Arrays.asList(shelf1, shelf2);
        when(shelfRepository.findAllByAvailability(true)).thenReturn(availableShelves);

        List<Shelf> result = shelfService.getAllAvailableBooks();
        assertEquals(2, result.size());
        verify(shelfRepository, times(1)).findAllByAvailability(true);
    }
}
