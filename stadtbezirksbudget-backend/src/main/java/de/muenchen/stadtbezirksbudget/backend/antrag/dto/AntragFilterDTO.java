package de.muenchen.stadtbezirksbudget.backend.antrag.dto;

import de.muenchen.stadtbezirksbudget.backend.antrag.entity.AktualisierungArt;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Status;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Data Transfer Object (DTO) for filtering Antrag entities.
 *
 * This DTO contains various filtering criteria that can be applied to query Antrag entities,
 * including status, Bezirksausschuss number, date ranges, Antragsteller name, Projekt title,
 * budget ranges, and update information.
 */
public record AntragFilterDTO(
        List<Status> status,
        List<Integer> bezirksausschussNr,
        LocalDateTime eingangDatumVon,
        LocalDateTime eingangDatumBis,
        String antragstellerName,
        String projektTitel,
        BigDecimal beantragtesBudgetVon,
        BigDecimal beantragtesBudgetBis,
        Boolean istFehlbetrag,
        List<AktualisierungArt> aktualisierungArt,
        LocalDateTime aktualisierungDatumVon,
        LocalDateTime aktualisierungDatumBis) {
}
