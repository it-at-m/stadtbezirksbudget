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
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents a request that contains various attributes such as the date of receipt,
 * the responsible project, and the requester.
 */
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class Antrag extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 1L;

    @Positive private int bezirksausschussNr;
    @NotNull private LocalDateTime eingangDatum;
    private boolean istPersonVorsteuerabzugsberechtigt;
    private boolean istAndererZuwendungsantrag;

    @NotNull @OneToOne
    private Bearbeitungsstand bearbeitungsstand;

    @NotNull @Enumerated(EnumType.STRING)
    private AktualisierungArt aktualisierungArt;

    @NotNull private String zammadTicketNr;

    @NotNull private LocalDateTime aktualisierungDatum;

    @NotNull private String aktenzeichen;

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
