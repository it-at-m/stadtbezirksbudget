package de.muenchen.stadtbezirksbudget.backend.antrag.dto.antrag_details;

import de.muenchen.stadtbezirksbudget.backend.common.ExcludedFromGeneratedCoverage;
import java.time.LocalDate;

/**
 * Data Transfer Object (DTO) representing the information of a Projekt.
 */
@ExcludedFromGeneratedCoverage(reason = "DTO is a pure data carrier (no logic) and therefore unreasonable to test.")
public record ProjektDetailsDTO(
        String titel, // Dopplung mit AllgemeinDTO
        String rubrik, // Dopplung mit AllgemeinDTO
        String beschreibung,
        LocalDate start,
        LocalDate end,
        String fristBruchBegruendung
) {
}
