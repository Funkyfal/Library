import org.example.kafka.ShelfKafkaConsumer;
import org.example.services.ShelfService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ShelfKafkaConsumerValidateTest {

    @Mock
    private ShelfService shelfService;

    @InjectMocks
    private ShelfKafkaConsumer shelfKafkaConsumer;

    @Test
    void testHandleValidateResponse_Success() {
        String message = "30:true";
        shelfKafkaConsumer.handleValidateResponse(message);
        verify(shelfService, times(1)).addBookToShelf(30L);
    }

    @Test
    void testHandleValidateResponse_Failure() {
        String message = "40:false";
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            shelfKafkaConsumer.handleValidateResponse(message);
        });
        assertEquals("No such a book in a stock", exception.getMessage());
    }
}
