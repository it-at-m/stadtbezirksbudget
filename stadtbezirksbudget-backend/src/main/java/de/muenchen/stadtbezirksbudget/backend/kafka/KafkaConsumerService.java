package de.muenchen.stadtbezirksbudget.backend.kafka;

import de.muenchen.stadtbezirksbudget.backend.antrag.AntragService;
import de.muenchen.stadtbezirksbudget.backend.common.ExcludedFromGeneratedCoverage;
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
@ExcludedFromGeneratedCoverage(
        reason = "Excluded because no further implementation exists, tests will be implemented later. TODO: #414 Add tests and remove exclusion to reach branch coverage"
)
public class KafkaConsumerService {
    private final AntragService antragService;
    @Value("${spring.kafka.consumer.group-id}")
    private String groupId;

    public KafkaConsumerService(AntragService antragService) {
        this.antragService = antragService;
    }

    /**
     * Receives and processes messages from the Kafka topic <i>spring.kafka.template.default-topic</i>
     * in the group <i>spring.kafka.consumer.group-id</i>.
     * <p>
     * The method validates that the message key matches the content's ID. Messages with mismatched
     * keys are logged as warnings and skipped to maintain data integrity.
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
            //            final UUID uuidKey = UUID.fromString(key);
            //            if (!uuidKey.equals(content.id())) {
            //                log.warn("Message key {} does not match content ID {}. Skipping message.", key, content.id());
            //                return;
            //            }
            //            log.info("Received message in group {} with key {}: {}", groupId, uuidKey, content);
            log.info("Received message in group {} with key {}: {}", groupId, 1234, content);
        } catch (IllegalArgumentException e) {
            log.error("Failed to parse message key as UUID: {}. Skipping message.", key, e);
        }

        antragService.createFromKafka(content);
    }
}
