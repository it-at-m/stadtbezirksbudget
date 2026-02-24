package de.muenchen.stadtbezirksbudget.backend.antrag.dto.antrag_details;

import de.muenchen.stadtbezirksbudget.backend.antrag.entity.AndererZuwendungsantragStatus;
import de.muenchen.stadtbezirksbudget.backend.common.ExcludedFromGeneratedCoverage;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Data Transfer Object (DTO) representing the information of AndererZuwendungsantrag.
 */
@ExcludedFromGeneratedCoverage(reason = "DTO is a pure data carrier (no logic) and therefore unreasonable to test.")
public record AndererZuwendungsantragDTO(
        LocalDate antragDatum,
        String stelle,
        BigDecimal betrag,
        AndererZuwendungsantragStatus status) {
}
