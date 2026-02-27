package de.muenchen.stadtbezirksbudget.backend.antrag.dto.antrag_details;

import de.muenchen.stadtbezirksbudget.backend.common.ExcludedFromGeneratedCoverage;
import jakarta.validation.constraints.NotNull;

/**
 * Data Transfer Object (DTO) representing the information of a Vertretungsberechtigter.
 */
@ExcludedFromGeneratedCoverage(reason = "DTO is a pure data carrier and therefore unreasonable to test.")
public record AntragVertretungsberechtigterDTO(
        String nachname,
        String vorname,
        String strasseHausnummer,
        String ort,
        String postleitzahl,
        String weitereAngaben,
        String email,
        String telefonNr,
        String mobilNr) {

    @NotNull
    @Override
    public String toString() {
        return "AntragVertretungsberechtigterDTO[" +
                "nachname=" + nachname +
                ", vorname=" + vorname +
                ", strasseHausnummer=****" +
                ", ort=****" +
                ", postleitzahl=****" +
                ", weitereAngaben=****" +
                ", email=****" +
                ", telefonNr=****" +
                ", mobilNr=****" +
                "]";
    }
}
