package de.muenchen.stadtbezirksbudget.backend.antrag.dto.antrag_details;

import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Status;
import de.muenchen.stadtbezirksbudget.backend.common.ExcludedFromGeneratedCoverage;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Data Transfer Object (DTO) representing the common information of an Antrag.
 */
@ExcludedFromGeneratedCoverage(reason = "DTO is a pure data carrier (no logic) and therefore unreasonable to test.")
public record AntragAllgemeinDTO(
        String projektTitel,
        LocalDateTime eingangDatum,
        String antragstellerName,
        BigDecimal beantragtesBudget,
        String rubrik,
        Status status,
        String zammadNr,
        String aktenzeichen,
        String eakteCooAdresse,
        boolean istGegendert,
        String anmerkungen) {
}
