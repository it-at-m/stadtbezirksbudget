package de.muenchen.stadtbezirksbudget.backend.kafka;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * Dummy DTO for testing Kafka communication between CIT-EAI and backend modules.
 */
public record KafkaDTO(
        @NotBlank String nameAntragsteller,
        @NotBlank String geldinstitut,
        @NotNull @Positive int bezirksausschussNr) {
}
