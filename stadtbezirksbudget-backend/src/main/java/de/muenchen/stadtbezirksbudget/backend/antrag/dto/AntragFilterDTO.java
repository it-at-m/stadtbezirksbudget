package de.muenchen.stadtbezirksbudget.backend.antrag.dto;

import de.muenchen.stadtbezirksbudget.backend.antrag.entity.AktualisierungArt;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.FinanzierungArt;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Status;
import de.muenchen.stadtbezirksbudget.backend.common.ExcludedFromGeneratedCoverage;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Data Transfer Object (DTO) for filtering Antrag entities.
 * This DTO contains various filtering criteria that can be applied to query Antrag entities,
 * including status, Bezirksausschuss number, date ranges, Antragsteller name, Projekt title,
 * budget ranges, and update information.
 */
@ExcludedFromGeneratedCoverage(reason = "DTO is a pure data carrier (no logic) and therefore unreasonable to test.")
public record AntragFilterDTO(
        List<Status> status,
        List<Integer> bezirksausschussNr,
        LocalDateTime eingangDatumVon,
        LocalDateTime eingangDatumBis,
        String antragstellerName,
        String projektTitel,
        BigDecimal beantragtesBudgetVon,
        BigDecimal beantragtesBudgetBis,
        FinanzierungArt finanzierungArt,
        List<AktualisierungArt> aktualisierungArt,
        LocalDateTime aktualisierungDatumVon,
        LocalDateTime aktualisierungDatumBis) {

    public AntragFilterDTO {
        status = status != null ? List.copyOf(status) : List.of();
        bezirksausschussNr = bezirksausschussNr != null ? List.copyOf(bezirksausschussNr) : List.of();
        aktualisierungArt = aktualisierungArt != null ? List.copyOf(aktualisierungArt) : List.of();
    }
}
