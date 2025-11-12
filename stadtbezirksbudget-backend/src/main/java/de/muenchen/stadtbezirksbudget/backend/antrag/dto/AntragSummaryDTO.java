package de.muenchen.stadtbezirksbudget.backend.antrag.dto;

import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Status;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Data Transfer Object (DTO) representing a summary of an Antrag.
 */
public record AntragSummaryDTO(
        UUID id,
        Status status,
        String zammadNr,
        String aktenzeichen,
        int bezirksausschussNr,
        LocalDateTime eingangDatum,
        String antragstellerName,
        String projektTitel,
        BigDecimal beantragtesBudget,
        boolean istFehlbetrag,
        String aktualisierung,
        LocalDateTime aktualisierungDatum) {
}
