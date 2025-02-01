import org.example.kafka.BookKafkaConsumer;
import org.example.services.BookService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookKafkaConsumerTest {

    @Mock
    private BookService bookService;

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @InjectMocks
    private BookKafkaConsumer bookKafkaConsumer;

    @Test
    void testBookValidateResponse_whenBookExists() {
        bookKafkaConsumer.bookValidateResponse("10");
        verify(kafkaTemplate, times(1)).send("book-validate-response", "10:" +
                bookService.getBook(10L).isPresent());
    }

    @Test
    void testBookValidateResponse_withNonNumericId_shouldThrowException() {
        assertThrows(NumberFormatException.class, () -> {
            bookKafkaConsumer.bookValidateResponse("abc");
        });
    }
}
