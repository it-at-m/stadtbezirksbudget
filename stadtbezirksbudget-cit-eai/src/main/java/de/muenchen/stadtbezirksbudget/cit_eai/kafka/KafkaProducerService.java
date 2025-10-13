package de.muenchen.stadtbezirksbudget.cit_eai.kafka;

import de.muenchen.stadtbezirksbudget.common.KafkaDTO;
import lombok.RequiredArgsConstructor;
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
 * <li><b>spring.kafka.consumer.group-id</b>: The Kafka Consumer Group ID</li>
 * <li><b>spring.kafka.template.default-topic</b>: The Kafka topic name</li>
 * </ul>
 */
@Service
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
    public void publishMessage(final KafkaDTO content) {
        kafkaTemplate.send(topic, content.id().toString(), content);
    }
}
