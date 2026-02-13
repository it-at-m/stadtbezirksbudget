package de.muenchen.stadtbezirksbudget.backend.kafka;

import de.muenchen.stadtbezirksbudget.backend.antrag.AntragService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ValidationException;
import jakarta.validation.Validator;
import java.util.Set;
import java.util.function.Consumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;

/**
 * Configuration class for binding Kafka messages to the AntragService. It defines a Consumer bean
 * that processes incoming Kafka messages, validates them, and
 * delegates the creation of an Antrag to the AntragService.
 */
@Slf4j
@Configuration
public class AntragBindingConfig {

    /**
     * Defines a Consumer bean that processes Kafka messages containing KafkaDTO objects. It validates
     * the incoming message and, if valid, calls the
     * AntragService to create an Antrag.
     *
     * @param antragService the service responsible for handling Antrag creation logic
     * @param validator the validator used to validate the incoming KafkaDTO messages
     * @return a Consumer that processes Kafka messages and creates Anträge based on the content of the
     *         messages
     */
    @Bean
    public Consumer<Message<KafkaDTO>> antragConsumer(final AntragService antragService, final Validator validator) {
        return message -> {

            final KafkaDTO content = message.getPayload();
            final String topic = message.getHeaders().get(KafkaHeaders.RECEIVED_TOPIC, String.class);

            final Set<ConstraintViolation<KafkaDTO>> violations = validator.validate(content);
            if (!violations.isEmpty()) {
                final String violationMessages = violations.stream()
                        .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                        .reduce((m1, m2) -> m1 + ", " + m2)
                        .orElse("No violation messages");
                log.error("Validation failed for Kafka message with topic {}: {}", topic, violationMessages);
                throw new ValidationException(violationMessages);
            }

            log.info("Consuming kafka message from topic {}", topic);
            antragService.createFromKafka(content);
        };
    }
}
