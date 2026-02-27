package de.muenchen.stadtbezirksbudget.backend.antrag.dto.antrag_details;

import de.muenchen.stadtbezirksbudget.backend.common.ExcludedFromGeneratedCoverage;
import jakarta.validation.constraints.NotNull;

/**
 * Data Transfer Object (DTO) representing the information of Bankverbindung.
 */
@ExcludedFromGeneratedCoverage(reason = "DTO is a pure data carrier and therefore unreasonable to test.")
public record AntragBankverbindungDTO(
        boolean istVonVertretungsberechtigtem,
        String geldinstitut,
        String iban,
        String bic) {

    @NotNull
    @Override
    public String toString() {
        final String maskedIban = iban == null || iban.length() < 4
                ? "****"
                : "****" + iban.substring(iban.length() - 4);
        final String maskedBic = bic == null || bic.isBlank()
                ? null
                : "***";
        return "AntragBankverbindungDTO[" +
                "istVonVertretungsberechtigtem=" + istVonVertretungsberechtigtem +
                ", geldinstitut=" + geldinstitut +
                ", iban=" + maskedIban +
                ", bic=" + maskedBic +
                "]";
    }
}
