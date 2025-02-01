import org.example.kafka.BookKafkaProducer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookKafkaProducerTest {

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @InjectMocks
    private BookKafkaProducer bookKafkaProducer;

    @Test
    void testSendBookAction() {
        String action = "add";
        String bookId = "123";
        bookKafkaProducer.sendBookAction(action, bookId);
        verify(kafkaTemplate, times(1)).send("book-action", action + ":" + bookId);
    }
}
