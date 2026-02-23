package de.muenchen.stadtbezirksbudget.backend.antrag.integration;

import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Adresse;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.AktualisierungArt;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.AndererZuwendungsantrag;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Antrag;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Antragsteller;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Bankverbindung;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Bearbeitungsstand;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Bezirksinformationen;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Finanzierung;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.FinanzierungArt;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Finanzierungsmittel;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Kategorie;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Projekt;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Rechtsform;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Status;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Verwendungsnachweis;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.VoraussichtlicheAusgabe;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Zahlung;
import de.muenchen.stadtbezirksbudget.backend.antrag.repository.AndererZuwendungsantragRepository;
import de.muenchen.stadtbezirksbudget.backend.antrag.repository.AntragRepository;
import de.muenchen.stadtbezirksbudget.backend.antrag.repository.FinanzierungRepository;
import de.muenchen.stadtbezirksbudget.backend.antrag.repository.FinanzierungsmittelRepository;
import de.muenchen.stadtbezirksbudget.backend.antrag.repository.VoraussichtlicheAusgabeRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * Test helper for building and persisting Antrag entities with all required dependencies.
 * <p>
 * This builder is stateful and reusable. After calling {@code build()}, the builder
 * resets to default values, allowing it to be reused for creating multiple test entities:
 *
 * <pre>
 * Antrag first = builder.setBezirksausschussNr(1).build();
 * Antrag second = builder.setBezirksausschussNr(2).build(); // starts from defaults
 * </pre>
 */

@SuppressWarnings({ "PMD.AvoidFieldNameMatchingMethodName", "PMD.CouplingBetweenObjects" })
public class AntragBuilder {
    private static final int LIMIT = 100_000;
    private static final Random RANDOM = new Random();
    private final AntragRepository antragRepository;
    private final FinanzierungRepository finanzierungRepository;
    private final VoraussichtlicheAusgabeRepository voraussichtlicheAusgabeRepository;
    private final FinanzierungsmittelRepository finanzierungsmittelRepository;
    private final AndererZuwendungsantragRepository andererZuwendungsantragRepository;
    private Status status;
    private int bezirksausschussNr;
    private LocalDateTime eingangDatum;
    private LocalDateTime aktualisierungDatum;
    private BigDecimal beantragtesBudget;
    private FinanzierungArt finanzierungArt;
    private AktualisierungArt aktualisierungArt;
    private String zammadNr;
    private String aktenzeichen;
    private String eakteCooAdresse;
    private String antragstellerName;
    private String projektTitel;
    private List<AndererZuwendungsantrag> andereZuwendungsantraege;

    public AntragBuilder(final AntragRepository antragRepository,
            final FinanzierungRepository finanzierungRepository,
            final VoraussichtlicheAusgabeRepository voraussichtlicheAusgabeRepository,
            final FinanzierungsmittelRepository finanzierungsmittelRepository,
            final AndererZuwendungsantragRepository andererZuwendungsantragRepository) {
        this.antragRepository = antragRepository;
        this.finanzierungRepository = finanzierungRepository;
        this.voraussichtlicheAusgabeRepository = voraussichtlicheAusgabeRepository;
        this.finanzierungsmittelRepository = finanzierungsmittelRepository;
        this.andererZuwendungsantragRepository = andererZuwendungsantragRepository;
        setRandomValues();
    }

    private static String generateRandomUuidString() {
        return UUID.randomUUID().toString();
    }

    private void setRandomValues() {
        status = Status.values()[RANDOM.nextInt(Status.values().length)];
        bezirksausschussNr = RANDOM.nextInt(LIMIT);
        eingangDatum = LocalDateTime.now().withNano(0).minusDays(RANDOM.nextInt(LIMIT));
        aktualisierungDatum = LocalDateTime.now().withNano(0).minusDays(RANDOM.nextInt(LIMIT));
        beantragtesBudget = BigDecimal.valueOf(RANDOM.nextInt(LIMIT) / 100);
        finanzierungArt = FinanzierungArt.values()[RANDOM.nextInt(FinanzierungArt.values().length)];
        aktualisierungArt = AktualisierungArt.values()[RANDOM.nextInt(AktualisierungArt.values().length)];
        zammadNr = String.valueOf(RANDOM.nextInt(LIMIT));
        aktenzeichen = String.valueOf(RANDOM.nextInt(LIMIT));
        eakteCooAdresse = String.valueOf(RANDOM.nextInt(LIMIT));
        antragstellerName = generateRandomUuidString();
        projektTitel = generateRandomUuidString();
        andereZuwendungsantraege = new ArrayList<>();
    }

    public AntragBuilder status(final Status status) {
        this.status = status;
        return this;
    }

    public AntragBuilder bezirksausschussNr(final int bezirksausschussNr) {
        this.bezirksausschussNr = bezirksausschussNr;
        return this;
    }

    public AntragBuilder eingangDatum(final LocalDateTime datum) {
        this.eingangDatum = datum;
        return this;
    }

    public AntragBuilder aktualisierungDatum(final LocalDateTime datum) {
        this.aktualisierungDatum = datum;
        return this;
    }

    public AntragBuilder beantragtesBudget(final BigDecimal beantragtesBudget) {
        this.beantragtesBudget = beantragtesBudget;
        return this;
    }

    public AntragBuilder finanzierungArt(final FinanzierungArt finanzierungArt) {
        this.finanzierungArt = finanzierungArt;
        return this;
    }

    public AntragBuilder aktualisierungArt(final AktualisierungArt aktualisierungArt) {
        this.aktualisierungArt = aktualisierungArt;
        return this;
    }

    public AntragBuilder zammadNr(final String zammadNr) {
        this.zammadNr = zammadNr;
        return this;
    }

    public AntragBuilder aktenzeichen(final String aktenzeichen) {
        this.aktenzeichen = aktenzeichen;
        return this;
    }

    public AntragBuilder antragstellerName(final String antragstellerName) {
        this.antragstellerName = antragstellerName;
        return this;
    }

    public AntragBuilder projektTitel(final String projektTitel) {
        this.projektTitel = projektTitel;
        return this;
    }

    public AntragBuilder andererZuwendungsantrag(final List<AndererZuwendungsantrag> andereZuwendungsantraege) {
        this.andereZuwendungsantraege = andereZuwendungsantraege;
        return this;
    }

    public AntragBuilder eakteCooAdresse(final String eakteCooAdresse) {
        this.eakteCooAdresse = eakteCooAdresse;
        return this;
    }

    private Adresse initializeAdresse() {
        return Adresse.builder()
                .strasseHausnummer("Musterstraße 1")
                .postleitzahl("12345")
                .ort("München")
                .build();
    }

    private Antragsteller initializeAntragsteller(final Adresse adresse, final String name) {
        final Antragsteller antragsteller = Antragsteller.builder()
                .name(name)
                .zielsetzung("Förderung von Projekten ")
                .rechtsform(Rechtsform.NATUERLICHE_PERSON)
                .build();
        antragsteller.setTelefonNr("0123456789");
        antragsteller.setAdresse(adresse);
        antragsteller.setEmail("max@mustermann.de");
        return antragsteller;
    }

    private Projekt initializeProjekt(final String titel) {
        return Projekt.builder()
                .titel(titel)
                .beschreibung("Beschreibung des Projekts")
                .start(LocalDate.now())
                .ende(LocalDate.now().plusMonths(6))
                .fristBruchBegruendung("")
                .rubrik("Rubrik")
                .build();
    }

    private Bearbeitungsstand initializeBearbeitungsstand(final Status status) {
        return Bearbeitungsstand.builder()
                .anmerkungen("Antrag in Bearbeitung")
                .istMittelabruf(false)
                .status(status)
                .build();
    }

    private Finanzierung initializeFinanzierung(final BigDecimal beantragtesBudget, final FinanzierungArt finanzierungArt) {
        final Finanzierungsmittel finanzierungsmittel = Finanzierungsmittel.builder()
                .kategorie(Kategorie.EIGENMITTEL)
                .direktoriumNotiz("Notiz zu Finanzierungsmitteln")
                .build();

        final VoraussichtlicheAusgabe ausgabe = VoraussichtlicheAusgabe.builder()
                .kategorie("Material")
                .direktoriumNotiz("Notiz zu Materialausgaben")
                .build();

        // Calculate amounts based on whether finanzierungArt is FEHL or FEST based on the formula for finanzierungArt in Finanzierung-Entity
        switch (finanzierungArt) {
        case FEHL:
            // Formula needs to return FEHL: ausgabe > 5000 OR finanzierungsmittel > 0
            if (beantragtesBudget.compareTo(new BigDecimal(5000)) > 0) {
                ausgabe.setBetrag(beantragtesBudget);
                finanzierungsmittel.setBetrag(BigDecimal.ZERO);
            } else {
                ausgabe.setBetrag(beantragtesBudget);
                finanzierungsmittel.setBetrag(new BigDecimal(1000));
            }
            break;
        case FEST:
            // Formula needs to return FEST: ausgabe <= 5000 AND finanzierungsmittel = 0
            finanzierungsmittel.setBetrag(BigDecimal.ZERO);
            ausgabe.setBetrag(beantragtesBudget.min(new BigDecimal(5000)));
            break;
        }

        Finanzierung finanzierung = Finanzierung.builder()
                .istProjektVorsteuerabzugsberechtigt(true)
                .sonstigeFoerderhinweise("Keine")
                .beantragtesBudget(beantragtesBudget)
                .kostenAnmerkung("KostenAnmerkung")
                .begruendungEigenmittel("")
                .build();

        finanzierungsmittel.setFinanzierung(finanzierung);
        ausgabe.setFinanzierung(finanzierung);

        finanzierung.setFinanzierungsmittel(List.of(finanzierungsmittel));
        finanzierung.setVoraussichtlicheAusgaben(List.of(ausgabe));

        finanzierung = finanzierungRepository.save(finanzierung);
        voraussichtlicheAusgabeRepository.save(ausgabe);
        finanzierungsmittelRepository.save(finanzierungsmittel);

        return finanzierung;
    }

    private Bankverbindung initializeBankverbindung() {
        return Bankverbindung.builder()
                .istVonVertretungsberechtigtem(false)
                .geldinstitut("Musterbank")
                .iban("DE00123456789012345678")
                .bic("DUMMYBIC123")
                .build();
    }

    private Zahlung initializeZahlung() {
        return Zahlung.builder()
                .anlageNr("")
                .anlageAv("")
                .auszahlungBetrag(null)
                .auszahlungDatum(null)
                .bestellung("")
                .kreditor("")
                .fiBelegNr("")
                .rechnungNr("")
                .build();
    }

    private Verwendungsnachweis initializeVerwendungsnachweis() {
        return Verwendungsnachweis.builder()
                .betrag(null)
                .buchungsDatum(null)
                .pruefungBetrag(null)
                .sapEingangsdatum(null)
                .status("")
                .build();
    }

    private Bezirksinformationen initializeBezirksinformationen() {
        return Bezirksinformationen.builder()
                .bescheidDatum(null)
                .risNr("")
                .bewilligteFoerderung(null)
                .sitzungDatum(null)
                .build();
    }

    public Antrag build() {
        try {
            final Adresse adresse = initializeAdresse();
            final Antragsteller antragsteller = initializeAntragsteller(adresse, antragstellerName);
            final Antrag antrag = Antrag.builder()
                    .eingangDatum(eingangDatum)
                    .bezirksausschussNr(bezirksausschussNr)
                    .bearbeitungsstand(initializeBearbeitungsstand(status))
                    .aktualisierungArt(aktualisierungArt)
                    .zammadTicketNr(zammadNr)
                    .aktualisierungDatum(aktualisierungDatum)
                    .aktenzeichen(aktenzeichen)
                    .eakteCooAdresse(eakteCooAdresse)
                    .finanzierung(initializeFinanzierung(beantragtesBudget, finanzierungArt))
                    .projekt(initializeProjekt(projektTitel))
                    .antragsteller(antragsteller)
                    .bankverbindung(initializeBankverbindung())
                    .zahlung(initializeZahlung())
                    .verwendungsnachweis(initializeVerwendungsnachweis())
                    .bezirksinformationen(initializeBezirksinformationen())
                    .build();
            for (final AndererZuwendungsantrag andererZuwendungsantrag : andereZuwendungsantraege) {
                andererZuwendungsantrag.setAntrag(antrag);
            }
            antrag.setAndereZuwendungsantraege(andereZuwendungsantraege);
            final Antrag savedAntrag = antragRepository.save(antrag);
            andererZuwendungsantragRepository.saveAll(andereZuwendungsantraege);
            return savedAntrag;
        } finally {
            setRandomValues();
        }
    }
}
