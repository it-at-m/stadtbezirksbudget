package de.muenchen.stadtbezirksbudget.backend.antrag.dto.antrag_details;

import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Rechtsform;
import de.muenchen.stadtbezirksbudget.backend.common.ExcludedFromGeneratedCoverage;

/**
 * Data Transfer Object (DTO) representing the information of an Antragsteller.
 */
@ExcludedFromGeneratedCoverage(reason = "DTO is a pure data carrier and therefore unreasonable to test.")
public record AntragAntragstellerDTO(
        String vorname,
        String name,
        String telefonNr,
        String email,
        String zielsetzung,
        Rechtsform rechtsform,
        AdressDTO adresse) {
}
