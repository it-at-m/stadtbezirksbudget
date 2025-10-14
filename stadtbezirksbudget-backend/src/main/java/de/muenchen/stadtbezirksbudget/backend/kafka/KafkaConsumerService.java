package de.muenchen.stadtbezirksbudget.backend.kafka;

import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

/**
 * Service for consuming and processing messages from a Kafka topic.
 * This service listens to messages of type KafkaDTO from a predefined Kafka topic and processes
 * them.
 * <p>
 * <b>Used Application Properties:</b>
 * <ul>
 * <li><b>spring.kafka.consumer.group-id</b>: The Kafka Consumer Group ID</li>
 * <li><b>spring.kafka.template.default-topic</b>: The Kafka topic name</li>
 * </ul>
 * These properties are used via @Value injection and in the @KafkaListener annotation.
 */
@Service
@Slf4j
public class KafkaConsumerService {
    @Value("${spring.kafka.consumer.group-id}")
    private String groupId;

    /**
     * Receives and processes messages from the Kafka topic <i>spring.kafka.template.default-topic</i>
     * in the group <i>spring.kafka.consumer.group-id</i>.
     *
     * @param key the key of the received Kafka message as String
     * @param content the received message as KafkaDTO
     */
    @KafkaListener(topics = "${spring.kafka.template.default-topic}", groupId = "${spring.kafka.consumer.group-id}")
    public void listen(@Header(name = KafkaHeaders.RECEIVED_KEY) final String key, final KafkaDTO content) {
        if (key == null) {
            log.error("Received null message key. Skipping message: {}", content);
            return;
        }
        try {
            final UUID uuidKey = UUID.fromString(key);
            if (!uuidKey.equals(content.id())) {
                log.warn("Message key {} does not match content ID {}. Skipping message.", key, content.id());
                return;
            }
            log.info("Received message in group {} with key {}: {}", groupId, uuidKey, content);
        } catch (IllegalArgumentException e) {
            log.error("Failed to parse message key as UUID: {}. Skipping message.", key, e);
        }
    }
}
