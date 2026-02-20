package de.muenchen.stadtbezirksbudget.backend.antrag.dto.antrag_details;

import de.muenchen.stadtbezirksbudget.backend.common.ExcludedFromGeneratedCoverage;
import java.math.BigDecimal;
import java.util.List;

/**
 * Data Transfer Object (DTO) representing the information of Finanzierung (and related tables).
 */
@ExcludedFromGeneratedCoverage(reason = "DTO is a pure data carrier (no logic) and therefore unreasonable to test.")
public record FinanzierungDetailsDTO(
        boolean istPersonVorsteuerabzugsberechtigt,
        boolean istProjektVorsteuerabzugsberechtigt,
        List<VoraussichtlicheAusgabeDTO> voraussichtlicheAusgaben,
        BigDecimal gesamtKosten,
        String kostenAnmerkungen,
        boolean istZuwendungDritterBeantragt,
        List<AndererZuwendungsantragDTO> andererZuwendungsantraege,
        BigDecimal gesamtZuwendungenDritter,
        List<FinanzierungsmittelDTO> finanzierungsmittel,
        boolean istZuwenigEigenmittel,
        String begruendungEigenmittel,
        BigDecimal gesamtMittel,
        boolean istEinladungFoerderhinweis,
        boolean istWebsiteFoerderhinweis,
        boolean istSonstigerFoerderhinweis,
        String sonstigeFoerderhinweise) {

    public FinanzierungDetailsDTO {
        voraussichtlicheAusgaben = voraussichtlicheAusgaben != null ? List.copyOf(voraussichtlicheAusgaben) : List.of();
        andererZuwendungsantraege = andererZuwendungsantraege != null ? List.copyOf(andererZuwendungsantraege) : List.of();
        finanzierungsmittel = finanzierungsmittel != null ? List.copyOf(finanzierungsmittel) : List.of();
    }
}
