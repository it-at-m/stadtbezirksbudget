package de.muenchen.stadtbezirksbudget.backend.antrag;

import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Status;
import java.util.UUID;

public record AntragSummaryDTO(UUID id, Status antragsstatus, int bezirksausschussnummer, String eingangsdatum, String projekttitel,
                               String antragstellerName,
                               double beantragtesBudget, String anmerkungen, String bearbeiter) {
}
