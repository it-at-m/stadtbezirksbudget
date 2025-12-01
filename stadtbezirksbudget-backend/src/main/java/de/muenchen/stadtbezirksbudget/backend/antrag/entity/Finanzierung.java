package de.muenchen.stadtbezirksbudget.backend.antrag.entity;

import de.muenchen.stadtbezirksbudget.backend.common.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.io.Serial;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Formula;

/**
 * Represents the financing of a project.
 * Contains information about approved grants and various funding references.
 */
@Entity
@Getter
@Setter
public class Finanzierung extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 1L;

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

    @NotEmpty @OneToMany(mappedBy = "finanzierung")
    private List<VoraussichtlicheAusgabe> voraussichtlicheAusgaben = new ArrayList<>();

    @NotEmpty @OneToMany(mappedBy = "finanzierung")
    private List<Finanzierungsmittel> finanzierungsmittel = new ArrayList<>();

    //TODO: Rewriting calculation for istFehlbetrag as it is currently wrong #356
    //Ignored For Testing, as current calculation is wrong and will be changed.
    /**
     * This field represents whether there is a discrepancy (shortfall) between the calculated budget
     * and the requested budget.
     * The calculation is performed using SQL and checks if the sum of anticipated expenses
     * (`voraussichtlicheAusgabe`) minus
     * the sum of funding sources (`finanzierungsmittel`) equals the requested budget
     * (`beantragtesBudget`).
     * If the calculated shortfall is near zero, it means there is no discrepancy, and the field is set
     * to
     * true.
     * Otherwise, it is set to false.
     */
    @Formula(
        "(CASE WHEN " +
                "ABS(" +
                "(SELECT COALESCE(SUM(a.betrag), 0) FROM voraussichtliche_ausgabe a WHERE a.finanzierung_id = id) - " +
                "(SELECT COALESCE(SUM(m.betrag), 0) FROM finanzierungsmittel m WHERE m.finanzierung_id = id) - " +
                "beantragtes_budget" +
                ") < 0.001 " +
                "THEN true ELSE false END)"
    )
    private boolean istFehlbetrag;
}
