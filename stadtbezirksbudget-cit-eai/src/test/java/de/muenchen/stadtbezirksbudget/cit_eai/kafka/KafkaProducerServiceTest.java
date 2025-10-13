package de.muenchen.stadtbezirksbudget.cit_eai.kafka;

import static org.mockito.Mockito.verify;

import de.muenchen.stadtbezirksbudget.common.KafkaDTO;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;

@ExtendWith(MockitoExtension.class)
class KafkaProducerServiceTest {
    @Value("${spring.kafka.template.default-topic}")
    private String topic;
    @Mock
    private KafkaTemplate<String, KafkaDTO> kafkaTemplate;
    @InjectMocks
    private KafkaProducerService kafkaProducerService;

    @Test
    void testPublishMessage() {
        KafkaDTO kafkaDTO = new KafkaDTO(UUID.randomUUID(), "test message", 123);
        kafkaProducerService.publishMessage(kafkaDTO);
        verify(kafkaTemplate).send(topic, kafkaDTO.id().toString(), kafkaDTO);
    }
}
