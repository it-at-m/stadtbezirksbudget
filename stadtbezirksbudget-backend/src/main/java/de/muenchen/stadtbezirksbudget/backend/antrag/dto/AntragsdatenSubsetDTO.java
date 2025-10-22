package de.muenchen.stadtbezirksbudget.backend.antrag.dto;

import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Status;
import java.util.UUID;

public record AntragsdatenSubsetDTO(UUID id, Status antragsstatus, int bezirksausschussnummer, String eingangsdatum, String projekttitel,
        String antragstellerName,
        double beantragtesBudget, String anmerkungen, String bearbeiter) {
}
