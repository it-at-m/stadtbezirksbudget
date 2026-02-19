package de.muenchen.stadtbezirksbudget.backend.antrag.dto.antrag_details;

/**
 * Data Transfer Object (DTO) representing the information of Bankverbindung.
 */
public record BankverbindungDetailsDTO(
        String name, // Dopplung mit AllgemeinDTO, sofern Zerlegung in Vor- und Nachname im Frontend
        boolean istVonVertretungsberechtigtem, // Für Ermittlung Adressdaten
        String geldinstitut,
        String iban,
        String bic) {
}
