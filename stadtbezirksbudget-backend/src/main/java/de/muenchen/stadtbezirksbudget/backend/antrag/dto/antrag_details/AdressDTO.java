package de.muenchen.stadtbezirksbudget.backend.antrag.dto.antrag_details;

public record AdressDTO(
        String strasseHausnummer,
        String ort,
        String postleitzahl,
        String weitereAngaben) {
}
