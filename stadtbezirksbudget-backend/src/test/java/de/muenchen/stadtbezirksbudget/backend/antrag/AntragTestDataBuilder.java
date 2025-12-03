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
import java.util.stream.IntStream;

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

    private static final LocalDateTime FIXED_DATE_TIME = LocalDateTime.of(2010, 1, 1, 0, 0);

    private Adresse initializeAdresse(final int index) {
        final Adresse adresse = new Adresse();
        adresse.setStrasse("Musterstraße " + index);
        adresse.setHausnummer("1");
        adresse.setPostleitzahl("12345");
        adresse.setOrt("München");
        return adresseRepository.save(adresse);
    }

    private Antragsteller initializeAntragsteller(final Adresse adresse, final int index) {
        final Antragsteller antragsteller = new Antragsteller();
        antragsteller.setName("Max Mustermann " + (index % 10)); // Dopplungen alle 10 Anträge
        antragsteller.setZielsetzung("Förderung von Projekten " + index);
        antragsteller.setRechtsform(Rechtsform.NATUERLICHE_PERSON);
        antragsteller.setTelefonNr("0123456789");
        antragsteller.setEmail("max@mustermann.de");
        antragsteller.setAdresse(adresse);
        return antragstellerRepository.save(antragsteller);
    }

    private Projekt initializeProjekt(final int index) {
        final Projekt projekt = new Projekt();
        projekt.setTitel("Projekt XYZ " + (index % 10)); // Dopplungen alle 10 Anträge
        projekt.setBeschreibung("Beschreibung des Projekts " + index);
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

    private Finanzierung initializeFinanzierung(final BigDecimal beantragtesBudget, final int index) {
        final Finanzierung finanzierung = new Finanzierung();
        finanzierung.setIstProjektVorsteuerabzugsberechtigt(true);
        finanzierung.setSonstigeFoerderhinweise("Keine");

        final Finanzierungsmittel finanzierungsmittel = new Finanzierungsmittel();
        finanzierungsmittel.setKategorie(Kategorie.EIGENMITTEL);

        final VoraussichtlicheAusgabe ausgabe = new VoraussichtlicheAusgabe();
        ausgabe.setKategorie("Material");

        if (index % 2 == 0) {
            // Bei geraden Zahlen: istFehlbetrag soll true sein, daher Ausgabe-Mittel=beantragtes Budget.

            // Finanzierungsmittel auf die Hälfte des beantragten Budget setzen
            finanzierungsmittel.setBetrag(beantragtesBudget.divide(new BigDecimal(2), RoundingMode.HALF_UP));

            // VoraussichtlicheAusgabe auf Summe aus beantragtesBudget und Finanzierungsmittel setzen, damit Formel erfüllt ist.
            final BigDecimal ausgabenBetrag = beantragtesBudget.add(finanzierungsmittel.getBetrag());
            ausgabe.setBetrag(ausgabenBetrag);
        } else {
            // Bei ungeraden Zahlen: istFehlbetrag soll false sein, daher ziemlich alle Zahlen möglich. Hier 10_000 Mittel, Ausgaben in Höhe des beantragten Budgets
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

    public List<Antrag> initializeAntragList(final int count) {
        final List<Antrag> antragList = new ArrayList<>();
        final Status[] statuses = Status.values();
        final AktualisierungArt[] aktualisierungArts = AktualisierungArt.values();
        final int[] bezirksausschussNrs = IntStream.rangeClosed(1, 25).toArray();

        for (int i = 0; i < count; i++) {
            final Antrag antrag = new Antrag();
            final Adresse adresse = initializeAdresse(i);
            final Antragsteller antragsteller = initializeAntragsteller(adresse, i);

            // Dates are generated in following pattern: 01.01.2010 00:00:00, 31.12.2009 01:01:00, 30.12.2009 02:02:00, 29.12.2009 03:03:00, etc. for both eingangsdatum and aktualisierungsDatum
            antrag.setEingangDatum(FIXED_DATE_TIME.minusDays(i).plusHours(i % 24).plusMinutes(i % 60));
            antrag.setBezirksausschussNr(bezirksausschussNrs[i % bezirksausschussNrs.length]);
            antrag.setBearbeitungsstand(initializeBearbeitungsstand(statuses[i % statuses.length]));
            antrag.setAktualisierungArt(aktualisierungArts[i % aktualisierungArts.length]);
            antrag.setZammadTicketNr(String.format("%09d", i));
            antrag.setAktualisierungDatum(FIXED_DATE_TIME.minusDays(i).plusHours(i % 24).plusMinutes(i % 60));
            antrag.setAktenzeichen("0000.0-00-" + String.format("%04d", i));
            antrag.setFinanzierung(initializeFinanzierung(new BigDecimal((i + 1) * 1000), i));
            antrag.setProjekt(initializeProjekt(i));
            antrag.setAntragsteller(antragsteller);
            antrag.setBankverbindung(initializeBankverbindung(antragsteller));
            antrag.setAndereZuwendungsantraege(new ArrayList<>());
            antragList.add(antragRepository.save(antrag));
        }
        return antragList;
    }
}
