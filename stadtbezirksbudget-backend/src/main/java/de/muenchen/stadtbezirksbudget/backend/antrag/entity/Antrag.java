package de.muenchen.stadtbezirksbudget.backend.antrag.entity;

import de.muenchen.stadtbezirksbudget.backend.common.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.io.Serial;
import java.time.LocalDateTime;
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

    @NotNull private LocalDateTime eingangDatum;
    @NotNull private LocalDateTime aktualisierungDatum;
    @Positive private int bezirksausschussNr;
    @NotNull private String aktenzeichen;
    @NotNull private String zammadTicketNr;
    private boolean istPersonVorsteuerabzugsberechtigt;
    private boolean istAndererZuwendungsantrag;

    @NotNull @OneToOne
    private Bearbeitungsstand bearbeitungsstand;

    @NotNull @Enumerated(EnumType.STRING)
    private AktualisierungArt aktualisierungArt;

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
