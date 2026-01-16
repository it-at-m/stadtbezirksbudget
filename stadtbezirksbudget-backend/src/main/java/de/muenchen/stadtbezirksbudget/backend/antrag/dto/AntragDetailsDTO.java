package de.muenchen.stadtbezirksbudget.backend.antrag.dto;

import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Status;
import de.muenchen.stadtbezirksbudget.backend.common.ExcludedFromGeneratedCoverage;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Data Transfer Object (DTO) representing the details of an Antrag.
 */
@ExcludedFromGeneratedCoverage(reason = "DTO is a pure data carrier (no logic) and therefore unreasonable to test.")
public record AntragDetailsDTO(
        String projektTitel,
        LocalDateTime eingangDatum,
        String antragstellerName,
        BigDecimal beantragtesBudget,
        String rubrik,
        Status status,
        String zammadNr,
        String aktenzeichen,
        boolean istGegendert,
        String anmerkungen) {
}
