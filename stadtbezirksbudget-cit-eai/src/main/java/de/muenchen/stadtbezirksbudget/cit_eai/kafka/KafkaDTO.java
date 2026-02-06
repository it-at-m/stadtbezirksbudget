package de.muenchen.stadtbezirksbudget.cit_eai.kafka;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

/**
 * Dummy DTO for testing Kafka communication between CIT-EAI and backend modules.
 */
//public record KafkaDTO(@NotNull UUID id, String nameAntragsteller, String geldinstitut, Integer bezirksausschussNr) {
//}
public record KafkaDTO(String nameAntragsteller, String geldinstitut, Integer bezirksausschussNr) {
}