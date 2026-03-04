package de.muenchen.stadtbezirksbudget.backend.antrag.dto.antrag_details;

import de.muenchen.stadtbezirksbudget.backend.common.ExcludedFromGeneratedCoverage;
import java.math.BigDecimal;

/**
 * Data Transfer Object (DTO) representing the information of VoraussichtlicheAusgabe.
 */
@ExcludedFromGeneratedCoverage(reason = "DTO is a pure data carrier (no logic) and therefore unreasonable to test.")
public record VoraussichtlicheAusgabeDTO(
        String kategorie,
        BigDecimal betrag,
        String direktoriumNotiz) {
}
