package de.muenchen.stadtbezirksbudget.backend.antrag;

import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Status;

import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

public record AntragSummaryDTO(
        UUID id,
        Status antragsstatus,
        String zammadNummer,
        int bezirksausschussnummer,
        LocalDate eingangsdatum,
        String projekttitel,
        String antragstellerName,
        double beantragtesBudget,
        String aktualisierungsArt,
        Date datumAktualisierung,
        String anmerkungen,
        String bearbeiter) {
}
