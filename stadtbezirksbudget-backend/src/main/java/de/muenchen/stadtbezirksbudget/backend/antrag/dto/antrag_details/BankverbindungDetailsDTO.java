package de.muenchen.stadtbezirksbudget.backend.antrag.dto.antrag_details;

import de.muenchen.stadtbezirksbudget.backend.common.ExcludedFromGeneratedCoverage;

/**
 * Data Transfer Object (DTO) representing the information of Bankverbindung.
 */
@ExcludedFromGeneratedCoverage(reason = "DTO is a pure data carrier (no logic) and therefore unreasonable to test.")
public record BankverbindungDetailsDTO(
        boolean istVonVertretungsberechtigtem,
        String geldinstitut,
        String iban,
        String bic) {
}
