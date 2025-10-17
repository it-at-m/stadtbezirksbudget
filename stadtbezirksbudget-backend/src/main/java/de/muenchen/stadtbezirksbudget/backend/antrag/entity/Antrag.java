package de.muenchen.stadtbezirksbudget.backend.antrag.entity;

import de.muenchen.stadtbezirksbudget.backend.common.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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

/**
 * Repräsentiert einen Antrag, der verschiedene Attribute wie das Eingangsdatum,
 * den zuständigen Projekt und den Antragsteller enthält.
 */
@Entity
@Getter
@Setter
public class Antrag extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 1L;

    @PositiveOrZero private int bezirksausschussNr;
    @NotNull private LocalDate eingangsdatum;
    private boolean istPersonVorsteuerabzugsberechtigt;
    private boolean istAndererZuwendungsantrag;

    @NotNull @ManyToOne
    private Projekt projekt;

    @NotNull @OneToOne(fetch = FetchType.LAZY)
    private Finanzierung finanzierung;

    @NotNull @ManyToOne
    private Antragsteller antragsteller;

    @NotNull @ManyToOne
    private Bankverbindung bankverbindung;

    @NotNull @OneToOne
    private Bearbeitungsstand bearbeitungsstand;

    @ManyToOne
    private Vertretungsberechtigter vertretungsberechtigter;

    @OneToMany(mappedBy = "antrag")
    private List<AndererZuwendungsantrag> andereZuwendungsantraege;
}
