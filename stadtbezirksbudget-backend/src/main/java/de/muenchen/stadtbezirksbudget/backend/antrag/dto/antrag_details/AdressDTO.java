package de.muenchen.stadtbezirksbudget.backend.antrag.dto.antrag_details;

/**
 * Data Transfer Object (DTO) representing the information of Adresse.
 */
public record AdressDTO(
        String strasseHausnummer,
        String ort,
        String postleitzahl,
        String weitereAngaben) {
}
