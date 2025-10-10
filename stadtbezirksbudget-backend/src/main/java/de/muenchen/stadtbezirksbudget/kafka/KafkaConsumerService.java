package de.muenchen.stadtbezirksbudget.kafka;

import de.muenchen.stadtbezirksbudget.common.KafkaDTO;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

/**
 * Service for consuming and processing messages from a Kafka topic.
 * This service listens to the 'sbb-eai-topic' Kafka topic and processes incoming messages of type KafkaDTO.
 */
@Component
@Slf4j
public class KafkaConsumerService {
    /**
     * Receives and processes messages from the Kafka topic 'sbb-eai-topic'.
     *
     * @param key the key of the received Kafka message as String
     * @param content the received message as KafkaDTO
     */
    @KafkaListener(topics = "sbb-eai-topic", groupId = "sbb-backend")
    public void listen(@Header(name = KafkaHeaders.RECEIVED_KEY) String key, KafkaDTO content) {
        UUID uuidKey = UUID.fromString(key);
        log.info("Received message in group sbb-backend with key {}: {}", uuidKey, content.toString());
    }
}
