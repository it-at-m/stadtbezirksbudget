package de.muenchen.stadtbezirksbudget.backend.antrag.entity;

import de.muenchen.stadtbezirksbudget.backend.common.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.io.Serial;
import java.math.BigDecimal;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

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
    private List<VoraussichtlicheAusgabe> voraussichtlicheAusgaben;

    @NotEmpty @OneToMany(mappedBy = "finanzierung")
    private List<Finanzierungsmittel> finanzierungsmittel;

    /**
     * Calculates the requested budget by subtracting the total financing amounts
     * from the total anticipated expenses.
     *
     * @return the requested budget amount
     */
    public BigDecimal getBeantragtesBudget() {
        final BigDecimal ausgaben = voraussichtlicheAusgaben.stream()
                .map(VoraussichtlicheAusgabe::getBetrag).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
        final BigDecimal mittel = finanzierungsmittel.stream()
                .map(Finanzierungsmittel::getBetrag).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
        return ausgaben.subtract(mittel);
    }
}
