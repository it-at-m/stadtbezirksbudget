package de.muenchen.stadtbezirksbudget.backend.antrag.entity;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
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
@Getter
@Setter
@Builder
@Embeddable
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class Finanzierung {
    private static final String ART_FORMULA = "CASE " +
            "WHEN (SELECT COALESCE(SUM(a.betrag), 0) FROM voraussichtliche_ausgabe a WHERE a.antrag_id = id) > 5000 THEN 'FEHL' " +
            "WHEN (SELECT COALESCE(SUM(m.betrag), 0) FROM finanzierungsmittel m WHERE m.antrag_id = id) > 0 THEN 'FEHL' " +
            "ELSE 'FEST' END";

    private boolean istProjektVorsteuerabzugsberechtigt;
    @NotNull private String kostenAnmerkung;
    @NotNull @PositiveOrZero private BigDecimal summeAusgaben;
    @NotNull @PositiveOrZero private BigDecimal summeFinanzierungsmittel;
    @NotNull private String begruendungEigenmittel;
    @NotNull @PositiveOrZero private BigDecimal beantragtesBudget;
    private boolean istEinladungFoerderhinweis;
    private boolean istWebsiteFoerderhinweis;
    private boolean istSonstigerFoerderhinweis;
    @NotNull private String sonstigeFoerderhinweise;
    @PositiveOrZero private BigDecimal bewilligterZuschuss;

    @NotEmpty @OneToMany(mappedBy = "antrag")
    @Builder.Default
    private List<VoraussichtlicheAusgabe> voraussichtlicheAusgaben = new ArrayList<>();

    @NotEmpty @OneToMany(mappedBy = "antrag")
    @Builder.Default
    private List<Finanzierungsmittel> finanzierungsmittel = new ArrayList<>();

    @Formula(ART_FORMULA)
    @Enumerated(EnumType.STRING)
    private FinanzierungArt art;
}
