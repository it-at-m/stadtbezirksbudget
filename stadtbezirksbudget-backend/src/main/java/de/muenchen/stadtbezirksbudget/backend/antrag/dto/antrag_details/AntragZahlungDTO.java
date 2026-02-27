package de.muenchen.stadtbezirksbudget.backend.antrag.dto.antrag_details;

import de.muenchen.stadtbezirksbudget.backend.common.ExcludedFromGeneratedCoverage;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Data Transfer Object (DTO) representing the information of Zahlung.
 */
@ExcludedFromGeneratedCoverage(reason = "DTO is a pure data carrier (no logic) and therefore unreasonable to test.")
public record AntragZahlungDTO(
        BigDecimal auszahlungBetrag,
        LocalDate auszahlungDatum,
        String anlageAv,
        String anlageNr,
        String kreditor,
        String rechnungNr,
        String fiBelegNr,
        String bestellung) {
}
