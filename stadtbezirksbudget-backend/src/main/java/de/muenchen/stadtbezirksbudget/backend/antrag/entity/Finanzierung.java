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
    private boolean istEinladungsFoerderhinweis;
    private boolean istWebsiteFoerderhinweis;
    private boolean istSonstigerFoerderhinweis;
    @NotNull private String sonstigeFoerderhinweise;
    @PositiveOrZero private BigDecimal bewilligterZuschuss;

    @NotEmpty @OneToMany(mappedBy = "finanzierung")
    private List<VoraussichtlicheAusgabe> voraussichtlicheAusgaben;

    @NotEmpty @OneToMany(mappedBy = "finanzierung")
    private List<Finanzierungsmittel> finanzierungsmittelListe;
}
