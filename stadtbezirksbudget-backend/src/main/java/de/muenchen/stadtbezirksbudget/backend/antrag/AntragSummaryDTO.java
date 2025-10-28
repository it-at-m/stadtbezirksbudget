package de.muenchen.stadtbezirksbudget.backend.antrag;

import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Status;
import java.util.Date;
import java.util.UUID;

public record AntragSummaryDTO(
        UUID id,
        Status antragsstatus,
        String zammadNummer,
        int bezirksausschussnummer,
        Date eingangDatum,
        String projekttitel,
        String antragstellerName,
        double beantragtesBudget,
        String aktualisierung,
        Date aktualisierungDatum,
        String anmerkungen,
        String bearbeiter) {
}
