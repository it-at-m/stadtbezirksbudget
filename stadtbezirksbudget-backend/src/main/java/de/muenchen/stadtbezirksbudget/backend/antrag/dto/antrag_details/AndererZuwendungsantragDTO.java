package de.muenchen.stadtbezirksbudget.backend.antrag.dto.antrag_details;

import de.muenchen.stadtbezirksbudget.backend.antrag.entity.AndererZuwendungsantragStatus;
import java.math.BigDecimal;
import java.time.LocalDate;

public record AndererZuwendungsantragDTO(
        LocalDate antragDatum,
        String stelle,
        BigDecimal betrag,
        AndererZuwendungsantragStatus status) {
}
