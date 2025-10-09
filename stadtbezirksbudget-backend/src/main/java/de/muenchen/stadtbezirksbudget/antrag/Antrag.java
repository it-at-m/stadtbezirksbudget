package de.muenchen.stadtbezirksbudget.antrag;

import de.muenchen.stadtbezirksbudget.common.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import java.util.Date;
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
public class Antrag extends BaseEntity {
    private int bezirksausschussNr;
    private Date eingangsdatum;
    private String zuwendungenDritterBeschreibung;
    private boolean istPersonVorsteuerabzugsberechtigt;

    @OneToOne
    private Projekt projekt;

    @OneToOne
    private Finanzierung finanzierung;

    @ManyToOne
    private Antragsteller antragsteller;

    @ManyToOne
    private Bankverbindung bankverbindung;

    @ManyToOne
    private Vertretungsberechtigter vertretungsberechtigter; //TODO 1 -- 0..1 Verbindung

    @ManyToMany
    private List<Mitglied> mitglieder; //TODO 1..n--0||2..n Verbindung
}
