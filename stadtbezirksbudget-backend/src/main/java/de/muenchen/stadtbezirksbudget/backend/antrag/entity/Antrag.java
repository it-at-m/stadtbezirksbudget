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
 * Represents a request that contains various attributes such as the date of receipt,
 * the responsible project, and the requester.
 */
@Entity
@Getter
@Setter
public class Antrag extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull private LocalDate eingangsdatum;
    @PositiveOrZero private int bezirksausschussNr;
    private boolean istPersonVorsteuerabzugsberechtigt;
    private boolean istAndererZuwendungsantrag;

    @NotNull @OneToOne
    private Bearbeitungsstand bearbeitungsstand;

    @NotNull @OneToOne(fetch = FetchType.LAZY)
    private Finanzierung finanzierung;

    @NotNull @ManyToOne
    private Projekt projekt;

    @NotNull @ManyToOne
    private Antragsteller antragsteller;

    @NotNull @ManyToOne
    private Bankverbindung bankverbindung;

    @ManyToOne
    private Vertretungsberechtigter vertretungsberechtigter;

    @OneToMany(mappedBy = "antrag")
    private List<AndererZuwendungsantrag> andereZuwendungsantraege;
}
