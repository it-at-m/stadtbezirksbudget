package de.muenchen.stadtbezirksbudget.backend.antrag.dto.antrag_details;

/**
 * Data Transfer Object (DTO) representing the information of Bankverbindung.
 */
public record BankverbindungDetailsDTO(
        boolean istVonVertretungsberechtigtem,
        String geldinstitut,
        String iban,
        String bic) {
}
