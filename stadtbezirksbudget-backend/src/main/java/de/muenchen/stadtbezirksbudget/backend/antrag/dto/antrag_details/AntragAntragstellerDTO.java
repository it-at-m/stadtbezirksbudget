package de.muenchen.stadtbezirksbudget.backend.antrag.dto.antrag_details;

import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Rechtsform;
import de.muenchen.stadtbezirksbudget.backend.common.ExcludedFromGeneratedCoverage;
import jakarta.validation.constraints.NotNull;

/**
 * Data Transfer Object (DTO) representing the information of an Antragsteller.
 */
@ExcludedFromGeneratedCoverage(reason = "DTO is a pure data carrier and therefore unreasonable to test.")
public record AntragAntragstellerDTO(
        Rechtsform rechtsform,
        String strasseHausnummer,
        String ort,
        String postleitzahl,
        String weitereAngaben,
        String email,
        String telefonNr,
        String zielsetzung) {

    @NotNull @Override
    public String toString() {
        return "AntragAntragstellerDTO[" +
                "rechtsform=" + rechtsform +
                ", strasseHausnummer=****" +
                ", ort=****" +
                ", postleitzahl=****" +
                ", weitereAngaben=****" +
                ", email=****" +
                ", telefonNr=****" +
                ", zielsetzung=" + zielsetzung +
                "]";
    }
}
