package de.muenchen.stadtbezirksbudget.backend.antrag.dto.antrag_details;

import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Rechtsform;
import de.muenchen.stadtbezirksbudget.backend.common.ExcludedFromGeneratedCoverage;

/**
 * Data Transfer Object (DTO) representing the information of an Antragsteller.
 */
@ExcludedFromGeneratedCoverage(reason = "DTO is a pure data carrier (no logic) and therefore unreasonable to test.")
public record AntragstellerDetailsDTO(
        Rechtsform rechtsform,
        String strasseHausnummer,
        String ort,
        String postleitzahl,
        String weitereAngaben,
        String email,
        String telefon,
        String zielsetzung) {
}
