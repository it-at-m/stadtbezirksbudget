package de.muenchen.stadtbezirksbudget.antrag.entities;

import de.muenchen.stadtbezirksbudget.backend.common.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.io.Serial;
import java.time.LocalDate;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Antrag extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 1L;

    @PositiveOrZero private int bezirksausschussNr;
    @NotNull private LocalDate eingangsdatum;
    private boolean istPersonVorsteuerabzugsberechtigt;

    @NotNull @ManyToOne
    private Projekt projekt;

    @NotNull @OneToOne
    private Finanzierung finanzierung;

    @NotNull @ManyToOne
    private Antragsteller antragsteller;

    @NotNull @ManyToOne
    private Bankverbindung bankverbindung;

    @ManyToOne
    private Vertretungsberechtigter vertretungsberechtigter;

    @OneToMany
    private List<AndererZuwendungsantrag> andereZuwendungsantraege;
}
