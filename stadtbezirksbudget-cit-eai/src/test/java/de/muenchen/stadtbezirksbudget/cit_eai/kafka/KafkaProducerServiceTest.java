package de.muenchen.stadtbezirksbudget.cit_eai.kafka;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class KafkaProducerServiceTest {
    private static final String TOPIC = "test-topic";
    @Mock
    private KafkaTemplate<String, KafkaDTO> kafkaTemplate;
    @InjectMocks
    private KafkaProducerService kafkaProducerService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(kafkaProducerService, "topic", TOPIC);
    }

    @Nested
    class PublishMessage {
        @Test
        void testPublishMessageCallsSend() {
            final KafkaDTO kafkaDTO = new KafkaDTO("test name", "test message", 123);
            kafkaProducerService.publishMessage(kafkaDTO);
            ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
            verify(kafkaTemplate).send(eq(TOPIC), captor.capture(), eq(kafkaDTO));
            String usedKey = captor.getValue();
            assertNotNull(usedKey);
            assertDoesNotThrow(() -> UUID.fromString(usedKey));
        }
    }
}
