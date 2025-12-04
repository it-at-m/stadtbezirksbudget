package de.muenchen.stadtbezirksbudget.cit_eai.kafka;

import de.muenchen.stadtbezirksbudget.cit_eai.security.Authorities;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for publishing messages to Kafka.
 * Provides an endpoint for sending messages to a Kafka topic via the KafkaProducerService.
 * Only used for testing purposes. In production, messages will be sent directly from the service
 * layer.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/kafka")
public class KafkaProducerController {
    private final KafkaProducerService kafkaProducerService;

    /**
     * Publishes a message to Kafka.
     *
     * @param message the message to be published as KafkaDTO
     * @return confirmation text that the message was published successfully
     */
    @PreAuthorize(Authorities.KAFKA_PUBLISH_MESSAGE)
    @PostMapping("/publish")
    public String publishMessage(@RequestBody @Valid final KafkaDTO message) {
        kafkaProducerService.publishMessage(message);
        return "Message published successfully";
    }
}
