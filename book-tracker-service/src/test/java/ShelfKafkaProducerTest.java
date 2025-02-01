import org.example.kafka.ShelfKafkaProducer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ShelfKafkaProducerTest {

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @InjectMocks
    private ShelfKafkaProducer shelfKafkaProducer;

    @Test
    void testSendShelfAction() {
        String bookId = "123";
        shelfKafkaProducer.sendShelfAction(bookId);
        verify(kafkaTemplate, times(1)).send("book-validate", bookId);
    }
}
