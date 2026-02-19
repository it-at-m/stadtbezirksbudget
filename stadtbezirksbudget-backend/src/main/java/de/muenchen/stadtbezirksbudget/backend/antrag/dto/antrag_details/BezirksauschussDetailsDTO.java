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
        //TODO was ist BESCHLUSS??? Attribut muss hier noch rein
        BigDecimal bewilligteFoerderung,
        LocalDate bescheidDatum) {
}
