package de.muenchen.stadtbezirksbudget.backend.antrag.entity;

import de.muenchen.stadtbezirksbudget.backend.common.BaseEntity;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
import org.hibernate.annotations.EmbeddedColumnNaming;

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
@SuppressWarnings("PMD.TooManyFields")
public class Antrag extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 1L;

    @Positive private int bezirksausschussNr;
    @NotNull private LocalDateTime eingangDatum;
    private boolean istPersonVorsteuerabzugsberechtigt;
    @NotNull private String zammadTicketNr;
    @NotNull private LocalDateTime aktualisierungDatum;
    @NotNull private String aktenzeichen;
    @NotNull @Enumerated(EnumType.STRING)
    private AktualisierungArt aktualisierungArt;

    @NotNull @Embedded
    @EmbeddedColumnNaming("bearbeitungsstand_%s")
    private Bearbeitungsstand bearbeitungsstand;

    @NotNull @Embedded
    @EmbeddedColumnNaming("bezirksinformationen_%s")
    private Bezirksinformationen bezirksinformationen;

    @NotNull @Embedded
    @EmbeddedColumnNaming("verwendungsnachweis_%s")
    private Verwendungsnachweis verwendungsnachweis;

    @NotNull @Embedded
    @EmbeddedColumnNaming("zahlung_%s")
    private Zahlung zahlung;

    @NotNull @OneToOne
    private Finanzierung finanzierung;

    @NotNull @Embedded
    @EmbeddedColumnNaming("projekt_%s")
    private Projekt projekt;

    @NotNull @Embedded
    @EmbeddedColumnNaming("antragsteller_%s")
    private Antragsteller antragsteller;

    @NotNull @Embedded
    @EmbeddedColumnNaming("bankverbindung_%s")
    private Bankverbindung bankverbindung;

    @Embedded
    @EmbeddedColumnNaming("vertretungsberechtigter_%s")
    private Vertretungsberechtigter vertretungsberechtigter;

    @OneToMany(mappedBy = "antrag")
    private List<AndererZuwendungsantrag> andereZuwendungsantraege;
}
