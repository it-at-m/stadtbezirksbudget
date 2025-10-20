package de.muenchen.stadtbezirksbudget.backend.antrag.dto;

import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Status;
import java.time.LocalDate;
import java.util.UUID;

public record AntragsdatenSubsetDTO(UUID id, Status antragsstatus, int bezirksausschussnummer, LocalDate eingangsdatum, String projekttitel,
        String antragstellerName,
        double beantragtesBudget, String anmerkungen, String bearbeiter) {
}
