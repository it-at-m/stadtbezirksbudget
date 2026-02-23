package de.muenchen.stadtbezirksbudget.backend.antrag.entity;

import de.muenchen.stadtbezirksbudget.backend.common.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.io.Serial;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Formula;

/**
 * Represents the financing of a project.
 * Contains information about approved grants and various funding references.
 */
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class Finanzierung extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 1L;

    private static final String GESAMTKOSTEN_FORMULA = "(SELECT COALESCE(SUM(a.betrag), 0) FROM voraussichtliche_ausgabe a WHERE a.finanzierung_id = id)";
    private static final String GESAMTMITTEL_FORMULA = "(SELECT COALESCE(SUM(m.betrag), 0) FROM finanzierungsmittel m WHERE m.finanzierung_id = id)";

    private static final int FEHL_ART_SCHWELLE = 5000;
    private static final String ART_FORMULA = "CASE " +
            "WHEN " + GESAMTKOSTEN_FORMULA + " > " + FEHL_ART_SCHWELLE + " THEN 'FEHL' " +
            "WHEN " + GESAMTMITTEL_FORMULA + " > 0 THEN 'FEHL' " +
            "ELSE 'FEST' END";

    private static final double MINDEST_EIGENMITTEL_PROZENT = 0.25;
    private static final String ZUWENIG_EIGENMITTEL_FORMULA = "CASE WHEN " +
            "(SELECT COALESCE(SUM(m.betrag), 0) FROM finanzierungsmittel m WHERE m.finanzierung_id = id AND m.kategorie = 'EIGENMITTEL')" +
            " < " + MINDEST_EIGENMITTEL_PROZENT + " * " + GESAMTKOSTEN_FORMULA + " THEN true ELSE false END";

    private boolean istProjektVorsteuerabzugsberechtigt;
    private boolean istPersonVorsteuerabzugsberechtigt;
    @NotNull private String kostenAnmerkung;
    @NotNull private String begruendungEigenmittel;
    @NotNull @PositiveOrZero private BigDecimal beantragtesBudget;
    private boolean istEinladungFoerderhinweis;
    private boolean istWebsiteFoerderhinweis;
    private boolean istSonstigerFoerderhinweis;
    @NotNull private String sonstigeFoerderhinweise;

    @NotEmpty @OneToMany(mappedBy = "finanzierung")
    @Builder.Default
    private List<VoraussichtlicheAusgabe> voraussichtlicheAusgaben = new ArrayList<>();

    @NotEmpty @OneToMany(mappedBy = "finanzierung")
    @Builder.Default
    private List<Finanzierungsmittel> finanzierungsmittel = new ArrayList<>();

    @Formula(ART_FORMULA)
    @Enumerated(EnumType.STRING)
    private FinanzierungArt art;

    @Formula(GESAMTKOSTEN_FORMULA)
    private BigDecimal gesamtkosten;

    @Formula(GESAMTMITTEL_FORMULA)
    private BigDecimal gesamtmittel;

    @Formula(ZUWENIG_EIGENMITTEL_FORMULA)
    private boolean istZuwenigEigenmittel;

}
