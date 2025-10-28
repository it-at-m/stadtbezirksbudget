package de.muenchen.stadtbezirksbudget.backend.antrag.dto;

import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Status;
import java.time.LocalDateTime;
import java.util.UUID;

public record AntragSummaryDTO(
        UUID id,
        Status status,
        String zammadNr,
        int bezirksausschussNr,
        LocalDateTime eingangDatum,
        String projektTitel,
        String antragstellerName,
        double beantragtesBudget,
        String aktualisierung,
        LocalDateTime aktualisierungDatum,
        String anmerkungen,
        String bearbeiter) {
}
