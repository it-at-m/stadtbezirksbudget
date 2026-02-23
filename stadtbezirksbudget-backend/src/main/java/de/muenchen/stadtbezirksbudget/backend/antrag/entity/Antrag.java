package de.muenchen.stadtbezirksbudget.backend.antrag.entity;

import de.muenchen.stadtbezirksbudget.backend.common.BaseEntity;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;
import java.io.Serial;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.EmbeddedColumnNaming;
import org.hibernate.annotations.Formula;

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
    private static final String ZUWENDUNG_DRITTER_BEANTRAGT_FORMULA = "(SELECT EXISTS (SELECT 1 FROM anderer_zuwendungsantrag az WHERE az.antrag_id = id))";
    private static final String SUMME_ANDERE_ZUWENDUNGSANTRAEGE_FORMULA = "(SELECT COALESCE(SUM(az.betrag), 0) FROM anderer_zuwendungsantrag az WHERE az.antrag_id = id)";

    private static final String BEANTRAGT = "(SELECT f.beantragtes_budget FROM finanzierung f WHERE f.id = finanzierung_id)";
    private static final String BEWILLIGT = "(SELECT bezirksinformationen_bewilligte_foerderung FROM antrag a WHERE a.id = id)";
    private static final String BESCHLUSS_STATUS_FORMULA = "(CASE " +
            "   WHEN " + BEWILLIGT + " IS NULL THEN 'LEER' " +
            "   WHEN " + BEWILLIGT + " = 0 THEN 'ABGELEHNT' " +
            "   WHEN " + BEWILLIGT + " = " + BEANTRAGT + " THEN 'BEWILLIGT' " +
            "   ELSE 'TEILBEWILLIGT' " +
            "END)";

    @NotNull private LocalDateTime eingangDatum;
    private boolean istGegendert;
    @NotNull private String zammadTicketNr;
    @NotNull private String aktenzeichen;
    @NotNull private String eakteCooAdresse;

    @NotNull private LocalDateTime aktualisierungDatum;
    @NotNull @Enumerated(EnumType.STRING)
    private AktualisierungArt aktualisierungArt;

    @Formula(ZUWENDUNG_DRITTER_BEANTRAGT_FORMULA)
    private boolean istZuwendungDritterBeantragt;

    @Formula(SUMME_ANDERE_ZUWENDUNGSANTRAEGE_FORMULA)
    private BigDecimal summeAndereZuwendungsantraege;

    @Formula(BEANTRAGT)
    private BigDecimal beantragt;

    @Formula(BEWILLIGT)
    private BigDecimal bewilligt;

    @Formula(BESCHLUSS_STATUS_FORMULA)
    @Enumerated(EnumType.STRING)
    private BeschlussStatus beschlussStatus;

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
