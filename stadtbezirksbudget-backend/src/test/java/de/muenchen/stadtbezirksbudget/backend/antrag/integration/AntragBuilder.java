package de.muenchen.stadtbezirksbudget.backend.antrag.integration;

import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Adresse;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.AktualisierungArt;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Antrag;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Antragsteller;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Bankverbindung;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Bearbeitungsstand;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Finanzierung;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Finanzierungsmittel;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Kategorie;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Projekt;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Rechtsform;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Status;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.VoraussichtlicheAusgabe;
import de.muenchen.stadtbezirksbudget.backend.antrag.repository.AdresseRepository;
import de.muenchen.stadtbezirksbudget.backend.antrag.repository.AntragRepository;
import de.muenchen.stadtbezirksbudget.backend.antrag.repository.AntragstellerRepository;
import de.muenchen.stadtbezirksbudget.backend.antrag.repository.BankverbindungRepository;
import de.muenchen.stadtbezirksbudget.backend.antrag.repository.BearbeitungsstandRepository;
import de.muenchen.stadtbezirksbudget.backend.antrag.repository.FinanzierungRepository;
import de.muenchen.stadtbezirksbudget.backend.antrag.repository.FinanzierungsmittelRepository;
import de.muenchen.stadtbezirksbudget.backend.antrag.repository.ProjektRepository;
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
@SuppressWarnings("PMD.CouplingBetweenObjects")
public class AntragBuilder {
    static final int limit = 100000;
    static final Random random = new Random();
    private final AntragRepository antragRepository;
    private final AdresseRepository adresseRepository;
    private final FinanzierungRepository finanzierungRepository;
    private final AntragstellerRepository antragstellerRepository;
    private final ProjektRepository projektRepository;
    private final BearbeitungsstandRepository bearbeitungsstandRepository;
    private final BankverbindungRepository bankverbindungRepository;
    private final FinanzierungsmittelRepository finanzierungsmittelRepository;
    private final VoraussichtlicheAusgabeRepository voraussichtlicheAusgabeRepository;
    private Status status;
    private int bezirksausschussNr;
    private LocalDateTime eingangDatum;
    private LocalDateTime aktualisierungDatum;
    private BigDecimal beantragtesBudget;
    private boolean istFehlbetrag;
    private AktualisierungArt aktualisierungArt;
    private String zammadNr;
    private String aktenzeichen;
    private String antragstellerName;
    private String projektTitel;

    public AntragBuilder(
            final AntragRepository antragRepository,
            final AdresseRepository adresseRepository,
            final FinanzierungRepository finanzierungRepository,
            final AntragstellerRepository antragstellerRepository,
            final ProjektRepository projektRepository,
            final BearbeitungsstandRepository bearbeitungsstandRepository,
            final BankverbindungRepository bankverbindungRepository,
            final FinanzierungsmittelRepository finanzierungsmittelRepository,
            final VoraussichtlicheAusgabeRepository voraussichtlicheAusgabeRepository) {
        this.antragRepository = antragRepository;
        this.adresseRepository = adresseRepository;
        this.finanzierungRepository = finanzierungRepository;
        this.antragstellerRepository = antragstellerRepository;
        this.projektRepository = projektRepository;
        this.bearbeitungsstandRepository = bearbeitungsstandRepository;
        this.bankverbindungRepository = bankverbindungRepository;
        this.finanzierungsmittelRepository = finanzierungsmittelRepository;
        this.voraussichtlicheAusgabeRepository = voraussichtlicheAusgabeRepository;
        setRandomValues();
    }

    private static String generateRandomUuidString() {
        return UUID.randomUUID().toString();
    }

    private void setRandomValues() {
        status = Status.values()[random.nextInt(Status.values().length)];
        bezirksausschussNr = random.nextInt(limit);
        eingangDatum = LocalDateTime.now().minusDays(random.nextInt(limit));
        aktualisierungDatum = LocalDateTime.now().minusDays(random.nextInt(limit));
        beantragtesBudget = BigDecimal.valueOf(random.nextInt(limit) / 100);
        istFehlbetrag = random.nextBoolean();
        aktualisierungArt = AktualisierungArt.values()[random.nextInt(AktualisierungArt.values().length)];
        zammadNr = String.valueOf(random.nextInt(limit));
        aktenzeichen = String.valueOf(random.nextInt(limit));
        antragstellerName = generateRandomUuidString();
        projektTitel = generateRandomUuidString();
    }

    public AntragBuilder setStatus(final Status status) {
        this.status = status;
        return this;
    }

    public AntragBuilder setBezirksausschussNr(final int bezirksausschussNr) {
        this.bezirksausschussNr = bezirksausschussNr;
        return this;
    }

    public AntragBuilder setEingangDatum(final LocalDateTime datum) {
        this.eingangDatum = datum;
        return this;
    }

    public AntragBuilder setAktualisierungDatum(final LocalDateTime datum) {
        this.aktualisierungDatum = datum;
        return this;
    }

    public AntragBuilder setBeantragtesBudget(final BigDecimal beantragtesBudget) {
        this.beantragtesBudget = beantragtesBudget;
        return this;
    }

    public AntragBuilder setIstFehlbetrag(final boolean istFehlbetrag) {
        this.istFehlbetrag = istFehlbetrag;
        return this;
    }

    public AntragBuilder setAktualisierungArt(final AktualisierungArt aktualisierungArt) {
        this.aktualisierungArt = aktualisierungArt;
        return this;
    }

    public AntragBuilder setZammadNr(final String zammadNr) {
        this.zammadNr = zammadNr;
        return this;
    }

    public AntragBuilder setAktenzeichen(final String aktenzeichen) {
        this.aktenzeichen = aktenzeichen;
        return this;
    }

    public AntragBuilder setAntragstellerName(final String antragstellerName) {
        this.antragstellerName = antragstellerName;
        return this;
    }

    public AntragBuilder setProjektTitel(final String projektTitel) {
        this.projektTitel = projektTitel;
        return this;
    }

    private Adresse initializeAdresse() {
        return adresseRepository.save(
                Adresse.builder()
                        // Generate random UUIDs to ensure unique strasse
                        .strasse("Musterstraße 1 " + generateRandomUuidString())
                        .hausnummer("1")
                        .postleitzahl("12345")
                        .ort("München")
                        .build());
    }

    private Antragsteller initializeAntragsteller(final Adresse adresse, final String name) {
        final Antragsteller antragsteller = Antragsteller.builder()
                .name(name)
                // Generate random UUIDs to ensure unique zielsetzung
                .zielsetzung("Förderung von Projekten " + generateRandomUuidString())
                .rechtsform(Rechtsform.NATUERLICHE_PERSON)
                .build();
        antragsteller.setTelefonNr("0123456789");
        antragsteller.setAdresse(adresse);
        antragsteller.setEmail("max@mustermann.de");
        return antragstellerRepository.save(antragsteller);
    }

    private Projekt initializeProjekt(final String titel) {
        return projektRepository.save(
                Projekt.builder()
                        .titel(titel)
                        // Generate random UUIDs to ensure unique beschreibung
                        .beschreibung("Beschreibung des Projekts, Titel: " + generateRandomUuidString())
                        .start(LocalDate.now())
                        .ende(LocalDate.now().plusMonths(6))
                        .fristBruchBegruendung("")
                        .build());
    }

    private Bearbeitungsstand initializeBearbeitungsstand(final Status status) {
        return bearbeitungsstandRepository.save(
                Bearbeitungsstand.builder()
                        .anmerkungen("Antrag in Bearbeitung")
                        .istMittelabruf(false)
                        .status(status)
                        .build());
    }

    private Finanzierung initializeFinanzierung(final BigDecimal beantragtesBudget, final boolean istFehlbetrag) {
        final Finanzierungsmittel finanzierungsmittel = Finanzierungsmittel.builder()
                .kategorie(Kategorie.EIGENMITTEL)
                .direktoriumNotiz("Notiz zu Finanzierungsmitteln")
                .build();

        final VoraussichtlicheAusgabe ausgabe = VoraussichtlicheAusgabe.builder()
                .kategorie("Material")
                .direktoriumNotiz("Notiz zu Materialausgaben")
                .build();

        // Calculate amounts based on whether istFehlbetrag is true or false based on the formula for istFehlbetrag in Finanzierung-Entity
        if (istFehlbetrag) {
            // Formula needs to be true: 2 * beantragtesBudget - 1 * beantragtesBudget = beantragtesBudget
            finanzierungsmittel.setBetrag(beantragtesBudget);
            ausgabe.setBetrag(beantragtesBudget.add(finanzierungsmittel.getBetrag()));
        } else {
            // Formula needs to be false: beantragtesBudget - 10_000 != beantragtesBudget
            finanzierungsmittel.setBetrag(new BigDecimal(10_000));
            ausgabe.setBetrag(beantragtesBudget);
        }

        Finanzierung finanzierung = Finanzierung.builder()
                .istProjektVorsteuerabzugsberechtigt(true)
                .sonstigeFoerderhinweise("Keine")
                .summeAusgaben(ausgabe.getBetrag())
                .summeFinanzierungsmittel(finanzierungsmittel.getBetrag())
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

    private Bankverbindung initializeBankverbindung(final Antragsteller antragsteller) {
        return bankverbindungRepository.save(
                Bankverbindung.builder()
                        .person("Max Mustermann")
                        .geldinstitut("Musterbank")
                        .iban("DE00123456789012345678")
                        .bic("DUMMYBIC123")
                        .zahlungsempfaenger(antragsteller)
                        .build());
    }

    public Antrag build() {
        try {
            final Adresse adresse = initializeAdresse();
            final Antragsteller antragsteller = initializeAntragsteller(adresse, antragstellerName);
            Antrag antrag = Antrag.builder()
                    .eingangDatum(eingangDatum)
                    .bezirksausschussNr(bezirksausschussNr)
                    .bearbeitungsstand(initializeBearbeitungsstand(status))
                    .aktualisierungArt(aktualisierungArt)
                    .zammadTicketNr(zammadNr)
                    .aktualisierungDatum(aktualisierungDatum)
                    .aktenzeichen(aktenzeichen)
                    .finanzierung(initializeFinanzierung(beantragtesBudget, istFehlbetrag))
                    .projekt(initializeProjekt(projektTitel))
                    .antragsteller(antragsteller)
                    .bankverbindung(initializeBankverbindung(antragsteller))
                    .andereZuwendungsantraege(new ArrayList<>())
                    .build();
            return antragRepository.save(antrag);
        } finally {
            setRandomValues();
        }
    }
}
