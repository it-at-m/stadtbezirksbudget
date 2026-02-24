package de.muenchen.stadtbezirksbudget.backend.antrag.dto.antrag_details;

import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Kategorie;
import de.muenchen.stadtbezirksbudget.backend.common.ExcludedFromGeneratedCoverage;
import java.math.BigDecimal;

/**
 * Data Transfer Object (DTO) representing the information of Finanzierungsmittel.
 */
@ExcludedFromGeneratedCoverage(reason = "DTO is a pure data carrier (no logic) and therefore unreasonable to test.")
public record FinanzierungsmittelDTO(
        Kategorie kategorie,
        BigDecimal betrag,
        String direktoriumNotiz) {
}
