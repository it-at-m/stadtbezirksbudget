package de.muenchen.stadtbezirksbudget.backend.antrag.dto;

import de.muenchen.stadtbezirksbudget.backend.antrag.entity.FinanzierungArt;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Status;
import de.muenchen.stadtbezirksbudget.backend.common.ExcludedFromGeneratedCoverage;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Data Transfer Object (DTO) representing a summary of an Antrag.
 */
@ExcludedFromGeneratedCoverage(reason = "DTO is a pure data carrier (no logic) and therefore unreasonable to test.")
public record AntragSummaryDTO(
        UUID id,
        Status status,
        String zammadNr,
        String aktenzeichen,
        String eakteCooAdresse,
        int bezirksausschussNr,
        LocalDateTime eingangDatum,
        String antragstellerName,
        String projektTitel,
        BigDecimal beantragtesBudget,
        FinanzierungArt finanzierungArt,
        String aktualisierung,
        LocalDateTime aktualisierungDatum) {
}
