package de.muenchen.stadtbezirksbudget.backend.antrag;

import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Status;
import java.time.LocalDateTime;
import java.util.UUID;

public record AntragSummaryDTO(
        UUID id,
        Status antragsstatus,
        String zammadNummer,
        int bezirksausschussnummer,
        LocalDateTime eingangDatum,
        String projekttitel,
        String antragstellerName,
        double beantragtesBudget,
        String aktualisierung,
        LocalDateTime aktualisierungDatum,
        String anmerkungen,
        String bearbeiter) {
}
