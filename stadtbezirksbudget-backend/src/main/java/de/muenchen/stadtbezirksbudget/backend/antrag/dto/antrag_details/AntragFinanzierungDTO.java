package de.muenchen.stadtbezirksbudget.backend.antrag.dto.antrag_details;

import de.muenchen.stadtbezirksbudget.backend.common.ExcludedFromGeneratedCoverage;
import java.math.BigDecimal;
import java.util.List;

/**
 * Data Transfer Object (DTO) representing the information of Finanzierung (and related tables).
 */
@ExcludedFromGeneratedCoverage(reason = "DTO is a pure data carrier and therefore unreasonable to test.")
public record AntragFinanzierungDTO(
        BigDecimal beantragtesBudget,
        boolean istPersonVorsteuerabzugsberechtigt,
        boolean istProjektVorsteuerabzugsberechtigt,
        List<VoraussichtlicheAusgabeDTO> voraussichtlicheAusgaben,
        BigDecimal gesamtkosten,
        String kostenAnmerkung,
        List<FinanzierungsmittelDTO> finanzierungsmittel,
        boolean istZuwenigEigenmittel,
        String begruendungEigenmittel,
        BigDecimal gesamtmittel,
        boolean istEinladungFoerderhinweis,
        boolean istWebsiteFoerderhinweis,
        boolean istSonstigerFoerderhinweis,
        String sonstigeFoerderhinweise) {

    public AntragFinanzierungDTO {
        voraussichtlicheAusgaben = voraussichtlicheAusgaben != null ? List.copyOf(voraussichtlicheAusgaben) : List.of();
        finanzierungsmittel = finanzierungsmittel != null ? List.copyOf(finanzierungsmittel) : List.of();
    }
}
