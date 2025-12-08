package de.muenchen.stadtbezirksbudget.backend.antrag;

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
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public record AntragTestDataBuilder(
        AntragRepository antragRepository,
        AdresseRepository adresseRepository,
        FinanzierungRepository finanzierungRepository,
        AntragstellerRepository antragstellerRepository,
        ProjektRepository projektRepository,
        BearbeitungsstandRepository bearbeitungsstandRepository,
        BankverbindungRepository bankverbindungRepository,
        FinanzierungsmittelRepository finanzierungsmittelRepository,
        VoraussichtlicheAusgabeRepository voraussichtlicheAusgabeRepository) {

    public static final Status DEFAULT_STATUS = Status.VOLLSTAENDIG;
    public static final int DEFAULT_BEZIRKSAUSSCHUSS_NR = 1;
    public static final LocalDateTime DEFAULT_DATUM = LocalDate.now().atStartOfDay();
    public static final BigDecimal DEFAULT_BEANTRAGTES_BUDGET = BigDecimal.valueOf(3000);
    public static final boolean DEFAULT_IST_FEHLBETRAG = false;
    public static final AktualisierungArt DEFAULT_AKTUALISIERUNG_ART = AktualisierungArt.E_AKTE;
    public static final String DEFAULT_ZAMMAD_NR = "000000000";
    public static final String DEFAULT_AKTENZEICHEN = "0000.0-00-0000";

    public static String getDefaultAntragstellerName() {
        return "Max Mustermann " + generateRandomUuidString();
    }

    public static String getDefaultProjektTitel() {
        return "Projekt XYZ " + generateRandomUuidString();
    }

    private static String generateRandomUuidString() {
        return UUID.randomUUID().toString();
    }

    private Adresse initializeAdresse() {
        final Adresse adresse = new Adresse();
        adresse.setStrasse("Musterstraße 1 " + generateRandomUuidString());
        adresse.setHausnummer("1");
        adresse.setPostleitzahl("12345");
        adresse.setOrt("München");
        return adresseRepository.save(adresse);
    }

    public Antrag initializeDefaultAntrag() {
        return initializeAntrag(
                DEFAULT_STATUS,
                DEFAULT_BEZIRKSAUSSCHUSS_NR,
                DEFAULT_DATUM,
                getDefaultAntragstellerName(),
                getDefaultProjektTitel(),
                DEFAULT_BEANTRAGTES_BUDGET,
                DEFAULT_IST_FEHLBETRAG,
                DEFAULT_AKTUALISIERUNG_ART,
                DEFAULT_DATUM,
                DEFAULT_ZAMMAD_NR,
                DEFAULT_AKTENZEICHEN);
    }

    public Antrag initializeAntrag(
            final Status status,
            final int bezirksausschussNr,
            final LocalDateTime eingangsDatum,
            final String antragstellerName,
            final String projektTitel,
            final BigDecimal beantragtesBudget,
            final boolean istFehlbetrag,
            final AktualisierungArt aktualisierungArt,
            final LocalDateTime aktualisierungDatum,
            final String zammadNr,
            final String aktenzeichen) {

        final Antrag antrag = new Antrag();
        final Adresse adresse = initializeAdresse();
        final Antragsteller antragsteller = initializeAntragsteller(adresse, antragstellerName);

        antrag.setEingangDatum(eingangsDatum);
        antrag.setBezirksausschussNr(bezirksausschussNr);
        antrag.setBearbeitungsstand(initializeBearbeitungsstand(status));
        antrag.setAktualisierungArt(aktualisierungArt);
        antrag.setZammadTicketNr(zammadNr);
        antrag.setAktualisierungDatum(aktualisierungDatum);
        antrag.setAktenzeichen(aktenzeichen);
        antrag.setFinanzierung(initializeFinanzierung(beantragtesBudget, istFehlbetrag));
        antrag.setProjekt(initializeProjekt(projektTitel));
        antrag.setAntragsteller(antragsteller);
        antrag.setBankverbindung(initializeBankverbindung(antragsteller));
        antrag.setAndereZuwendungsantraege(new ArrayList<>());
        return antragRepository.save(antrag);
    }

    private Antragsteller initializeAntragsteller(final Adresse adresse, final String name) {
        final Antragsteller antragsteller = new Antragsteller();
        antragsteller.setName(name);
        antragsteller.setZielsetzung("Förderung von Projekten");
        antragsteller.setRechtsform(Rechtsform.NATUERLICHE_PERSON);
        antragsteller.setTelefonNr("0123456789");
        antragsteller.setEmail("max@mustermann.de");
        antragsteller.setAdresse(adresse);
        return antragstellerRepository.save(antragsteller);
    }

    private Projekt initializeProjekt(final String titel) {
        final Projekt projekt = new Projekt();
        projekt.setTitel(titel);
        projekt.setBeschreibung("Beschreibung des Projekts, Titel: " + generateRandomUuidString());
        projekt.setStart(LocalDate.now());
        projekt.setEnde(LocalDate.now().plusMonths(6));
        projekt.setFristBruchBegruendung("");
        return projektRepository.save(projekt);
    }

    private Bearbeitungsstand initializeBearbeitungsstand(final Status status) {
        final Bearbeitungsstand bearbeitungsstand = new Bearbeitungsstand();
        bearbeitungsstand.setAnmerkungen("Antrag in Bearbeitung");
        bearbeitungsstand.setIstMittelabruf(false);
        bearbeitungsstand.setStatus(status);
        return bearbeitungsstandRepository.save(bearbeitungsstand);
    }

    private Finanzierung initializeFinanzierung(final BigDecimal beantragtesBudget, final boolean istFehlbetrag) {
        final Finanzierung finanzierung = new Finanzierung();
        finanzierung.setIstProjektVorsteuerabzugsberechtigt(true);
        finanzierung.setSonstigeFoerderhinweise("Keine");

        final Finanzierungsmittel finanzierungsmittel = new Finanzierungsmittel();
        finanzierungsmittel.setKategorie(Kategorie.EIGENMITTEL);

        final VoraussichtlicheAusgabe ausgabe = new VoraussichtlicheAusgabe();
        ausgabe.setKategorie("Material");

        if (istFehlbetrag) {
            // Set financing means to half of the requested budget
            finanzierungsmittel.setBetrag(beantragtesBudget.divide(new BigDecimal(2), RoundingMode.HALF_UP));

            // Set expected expenditure to the sum of requested budget and financing means to satisfy the formula.
            final BigDecimal ausgabenBetrag = beantragtesBudget.add(finanzierungsmittel.getBetrag());
            ausgabe.setBetrag(ausgabenBetrag);
        } else {
            // If istFehlbetrag is false, almost all numbers are possible. Here, 10.000 means, expenditure in the amount of the requested budget
            finanzierungsmittel.setBetrag(new BigDecimal(10_000));
            ausgabe.setBetrag(beantragtesBudget);
        }

        finanzierungsmittel.setDirektoriumNotiz("Notiz zu Finanzierungsmitteln");
        finanzierungsmittel.setFinanzierung(finanzierung);

        final List<Finanzierungsmittel> finanzierungsmittelListe = new ArrayList<>();
        finanzierungsmittelListe.add(finanzierungsmittel);
        finanzierung.setFinanzierungsmittel(finanzierungsmittelListe);

        ausgabe.setDirektoriumNotiz("Notiz zu Materialausgaben");
        ausgabe.setFinanzierung(finanzierung);

        final List<VoraussichtlicheAusgabe> voraussichtlicheAusgabenListe = new ArrayList<>();
        voraussichtlicheAusgabenListe.add(ausgabe);
        finanzierung.setVoraussichtlicheAusgaben(voraussichtlicheAusgabenListe);

        finanzierung.setSummeAusgaben(ausgabe.getBetrag());
        finanzierung.setSummeFinanzierungsmittel(finanzierungsmittel.getBetrag());
        finanzierung.setBeantragtesBudget(beantragtesBudget);

        finanzierung.setKostenAnmerkung("KostenAnmerkung");
        finanzierung.setBegruendungEigenmittel("");

        final Finanzierung finanzierungSaved = finanzierungRepository.save(finanzierung);

        voraussichtlicheAusgabeRepository.save(ausgabe);
        finanzierungsmittelRepository.save(finanzierungsmittel);

        return finanzierungSaved;
    }

    private Bankverbindung initializeBankverbindung(final Antragsteller antragsteller) {
        final Bankverbindung bankverbindung = new Bankverbindung();
        bankverbindung.setPerson("Max Mustermann");
        bankverbindung.setGeldinstitut("Musterbank");
        bankverbindung.setIban("DE00123456789012345678");
        bankverbindung.setBic("DUMMYBIC123");
        bankverbindung.setZahlungsempfaenger(antragsteller);

        return bankverbindungRepository.save(bankverbindung);
    }
}
