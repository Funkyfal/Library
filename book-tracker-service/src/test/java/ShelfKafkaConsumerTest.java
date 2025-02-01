import org.example.kafka.ShelfKafkaConsumer;
import org.example.services.ShelfService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ShelfKafkaConsumerTest {

    @Mock
    private ShelfService shelfService;

    @InjectMocks
    private ShelfKafkaConsumer shelfKafkaConsumer;

    @Test
    void testConsumeAddAction() {
        String message = "add:10";
        shelfKafkaConsumer.consumeBookId(message);
        verify(shelfService, times(1)).addBookToShelf(10L);
    }

    @Test
    void testConsumeDeleteAction() {
        String message = "delete:20";
        shelfKafkaConsumer.consumeBookId(message);
        verify(shelfService, times(1)).removeBookFromShelf(20L);
    }
}
