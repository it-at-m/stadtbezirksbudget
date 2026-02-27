package de.muenchen.stadtbezirksbudget.backend.antrag.dto.antrag_details;

import de.muenchen.stadtbezirksbudget.backend.common.ExcludedFromGeneratedCoverage;

/**
 * Data Transfer Object (DTO) representing the details of an Antrag.
 */
@ExcludedFromGeneratedCoverage(reason = "DTO is a pure data carrier (no logic) and therefore unreasonable to test.")
public record AntragDetailsDTO(
        AntragAllgemeinDTO allgemein,
        AntragAntragstellerDTO antragsteller,
        AntragBankverbindungDTO bankverbindung,
        AntragBezirksausschussDTO bezirksinformationen,
        AntragFinanzierungDTO finanzierung,
        AntragProjektDTO projekt,
        AntragVertretungsberechtigterDTO vertretungsberechtigter,
        AntragVerwendungsnachweisDTO verwendungsnachweis,
        AntragZahlungDTO zahlung) {
}
