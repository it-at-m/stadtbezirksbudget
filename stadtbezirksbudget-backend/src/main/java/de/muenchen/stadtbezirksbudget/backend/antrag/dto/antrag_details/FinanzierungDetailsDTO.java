package de.muenchen.stadtbezirksbudget.backend.antrag.dto.antrag_details;

import de.muenchen.stadtbezirksbudget.backend.antrag.entity.AndererZuwendungsantrag;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Finanzierungsmittel;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.VoraussichtlicheAusgabe;
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
        List<VoraussichtlicheAusgabe> voraussichtlicheAusgaben,
        BigDecimal gesamtKosten,
        String kostenAnmerkungen,
        boolean istZuwendungDritterBeantragt,
        List<AndererZuwendungsantrag> andererZuwendungsantraege,
        BigDecimal gesamtZuwendungenDritter,
        List<Finanzierungsmittel> finanzierungsmittel,
        boolean istZuwenigEigenmittel,
        String begruendungEigenmittel,
        BigDecimal gesamtMittel,
        boolean istEinladungFoerderhinweis,
        boolean istWebsiteFoerderhinweis,
        boolean istSonstigerFoerderhinweis,
        String sonstigeFoerderhinweise) {
}
