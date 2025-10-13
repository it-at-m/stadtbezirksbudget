package de.muenchen.stadtbezirksbudget.backend.kafka;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

/**
 * Dummy DTO for testing Kafka communication between CIT-EAI and backend modules.
 */
public record KafkaDTO(@NotNull UUID id, String param1, int param2) {
}
