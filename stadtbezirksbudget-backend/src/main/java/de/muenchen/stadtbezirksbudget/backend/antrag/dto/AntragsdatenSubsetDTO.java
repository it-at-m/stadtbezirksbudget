package de.muenchen.stadtbezirksbudget.backend.antrag.dto;

import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Status;
import java.time.LocalDate;

public record AntragsdatenSubsetDTO(Status antragsstatus, int bezirksausschussnummer, LocalDate eingangsdatum, String projekttitel, String antragstellerName,
        double beantragtesBudget, String anmerkungen, String bearbeiter) {
}
