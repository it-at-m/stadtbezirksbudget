package de.muenchen.stadtbezirksbudget.backend.antrag.dto.antrag_details;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Data Transfer Object (DTO) representing the information of Bezirksauschuss (and related tables).
 */
public record BezirksauschussDetailsDTO(
        int bezirksauschussNr,
        LocalDate sitzungDatum,
        String risNr,
        String beschluss,
        BigDecimal bewilligteFoerderung,
        LocalDate bescheidDatum) {
}
