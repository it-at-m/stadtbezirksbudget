package de.muenchen.stadtbezirksbudget.backend.antrag.entity;

import de.muenchen.stadtbezirksbudget.backend.common.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.io.Serial;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Finanzierung extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 1L;

    private boolean istProjektVorsteuerabzugsberechtigt;
    @PositiveOrZero private Double bewilligterZuschuss;
    private boolean istEinladungsFoerderhinweis;
    private boolean istWebsiteFoerderhinweis;
    @NotNull private String sonstigeFoerderhinweise;

    @NotEmpty @OneToMany
    private List<VoraussichtlicheAusgabe> voraussichtlicheAusgaben;

    @NotEmpty @OneToMany
    private List<Finanzierungsmittel> finanzierungsmittelListe;
}
