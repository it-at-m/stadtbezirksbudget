package de.muenchen.stadtbezirksbudget.backend.antrag.dto.antrag_details;

import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Kategorie;
import java.math.BigDecimal;

public record FinanzierungsmittelDTO(
        Kategorie kategorie,
        BigDecimal betrag,
        String direktoriumNotiz) {
}
