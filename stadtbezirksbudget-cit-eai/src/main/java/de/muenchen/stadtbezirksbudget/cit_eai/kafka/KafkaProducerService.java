package de.muenchen.stadtbezirksbudget.cit_eai.kafka;

import de.muenchen.stadtbezirksbudget.common.KafkaDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * Service for publishing messages to a Kafka topic.
 * This service provides functionality to send messages of type KafkaDTO to a predefined Kafka topic.
 */
@Service
@RequiredArgsConstructor
public class KafkaProducerService {
    private static final String TOPIC = "sbb-eai-topic";
    private final KafkaTemplate<String, KafkaDTO> kafkaTemplate;

    /**
     * Publishes a message to the Kafka topic.
     *
     * @param content the message to be published as KafkaDTO
     */
    public void publishMessage(KafkaDTO content) {
        kafkaTemplate.send(TOPIC, content.id().toString(), content);
    }
}
