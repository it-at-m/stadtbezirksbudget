package de.muenchen.stadtbezirksbudget.antrag.entities;

import de.muenchen.stadtbezirksbudget.common.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.Date;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Antrag extends BaseEntity {
    @NotEmpty @Positive private int bezirksausschussNr;
    @NotEmpty private Date eingangsdatum;
    @NotNull private String zuwendungenDritterBeschreibung;
    @NotEmpty private boolean istPersonVorsteuerabzugsberechtigt;

    @NotNull @OneToOne
    private Projekt projekt;

    @NotNull @OneToOne
    private Finanzierung finanzierung;

    @NotNull @ManyToOne
    private Antragsteller antragsteller;

    @NotNull @ManyToOne
    private Bankverbindung bankverbindung;

    @ManyToOne
    private Vertretungsberechtigter vertretungsberechtigter;

    @NotNull @OneToMany
    private List<Mitglied> mitglieder;
}
