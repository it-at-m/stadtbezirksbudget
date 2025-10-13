package de.muenchen.stadtbezirksbudget.cit_eai.kafka;

import static org.mockito.Mockito.verify;

import java.util.UUID;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

@ExtendWith(MockitoExtension.class)
class KafkaProducerServiceTest {
    private static final String TOPIC = "test-topic";
    @Mock
    private KafkaTemplate<String, KafkaDTO> kafkaTemplate;
    @InjectMocks
    private KafkaProducerService kafkaProducerService;

    @Nested
    class PublishMessage {
        @Test
        void testPublishMessageCallsSend() {
            final KafkaDTO kafkaDTO = new KafkaDTO(UUID.randomUUID(), "test message", 123);
            kafkaProducerService.publishMessage(kafkaDTO);
            verify(kafkaTemplate).send(TOPIC, kafkaDTO.id().toString(), kafkaDTO);
        }
    }
}
