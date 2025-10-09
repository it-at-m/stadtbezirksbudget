package de.muenchen.stadtbezirksbudget.antrag;

import de.muenchen.stadtbezirksbudget.common.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Finanzierung extends BaseEntity {
    private boolean istProjektVorsteuerabzugsberechtigt;
    private double bewilligterZuschuss;
    private boolean istEinladungsFoerderhinweis;
    private boolean istWebsiteFoerderhinweis;
    private String sonstigeFoerderhinweis;

    @OneToMany
    private List<VoraussichtlicheAusgabe> voraussichtlicheAusgaben;

    @OneToMany
    private List<Finanzierungsmittel> finanzierungsmittelListe;
}
