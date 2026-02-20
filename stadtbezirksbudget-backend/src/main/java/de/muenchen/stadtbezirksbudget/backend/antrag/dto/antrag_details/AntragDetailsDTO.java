package de.muenchen.stadtbezirksbudget.backend.antrag.dto.antrag_details;

import de.muenchen.stadtbezirksbudget.backend.common.ExcludedFromGeneratedCoverage;

/**
 * Data Transfer Object (DTO) representing the details of an Antrag.
 */
@ExcludedFromGeneratedCoverage(reason = "DTO is a pure data carrier (no logic) and therefore unreasonable to test.")
public record AntragDetailsDTO(
        AntragDetailsAllgemeinDTO allgemein,
        AntragstellerDetailsDTO antragsteller,
        BankverbindungDetailsDTO bankverbindung,
        BezirksauschussDetailsDTO bezirksauschuss,
        FinanzierungDetailsDTO finanzierung,
        ProjektDetailsDTO projekt,
        VertretungDetailsDTO vertretung,
        VerwendungDetailsDTO verwendung,
        ZahlungDetailsDTO zahlung) {
}
