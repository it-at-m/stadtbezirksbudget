package de.muenchen.stadtbezirksbudget.backend.antrag.dto.antrag_details;

import java.math.BigDecimal;

public record VoraussichtlicheAusgabeDTO(
        String kategorie,
        BigDecimal betrag,
        String direktoriumNotiz) {
}
