package de.muenchen.stadtbezirksbudget.cit_eai.kafka;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * Service for publishing messages to a Kafka topic.
 * This service provides functionality to send messages of type KafkaDTO to a predefined Kafka
 * topic.
 * <p>
 * <b>Used Application Properties:</b>
 * <ul>
 * <li><b>spring.kafka.template.default-topic</b>: The Kafka topic name</li>
 * </ul>
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaProducerService {
    private final KafkaTemplate<String, KafkaDTO> kafkaTemplate;
    @Value("${spring.kafka.template.default-topic}")
    private String topic;

    /**
     * Publishes a message to the Kafka topic defined in <i>spring.kafka.template.default-topic</i>.
     *
     * @param content the message to be published as KafkaDTO
     */
    public void publishMessage(@NotNull final KafkaDTO content) {
        final String id = UUID.randomUUID().toString();
        kafkaTemplate.send(topic, id, content);
        log.info("Published message to topic {} with key {}: {}", topic, id, content);
    }
}
