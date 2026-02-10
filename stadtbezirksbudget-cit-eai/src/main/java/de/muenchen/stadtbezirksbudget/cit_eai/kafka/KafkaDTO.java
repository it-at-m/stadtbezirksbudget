package de.muenchen.stadtbezirksbudget.cit_eai.kafka;

/**
 * Dummy DTO for testing Kafka communication between CIT-EAI and backend modules.
 */
public record KafkaDTO(String nameAntragsteller, String geldinstitut, Integer bezirksausschussNr) {
}
