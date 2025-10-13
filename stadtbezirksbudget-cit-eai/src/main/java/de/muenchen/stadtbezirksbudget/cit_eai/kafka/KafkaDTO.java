package de.muenchen.stadtbezirksbudget.cit_eai.kafka;

import java.util.UUID;

/**
 * Dummy DTO for testing Kafka communication between CIT-EAI and backend modules.
 */
public record KafkaDTO(UUID id, String param1, int param2) {
}
