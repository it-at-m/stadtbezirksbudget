package de.muenchen.stadtbezirksbudget.backend.antrag;

import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Adresse;
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
import de.muenchen.stadtbezirksbudget.backend.antrag.repository.BankverbindungRepository;
import de.muenchen.stadtbezirksbudget.backend.antrag.repository.BearbeitungsstandRepository;
import de.muenchen.stadtbezirksbudget.backend.antrag.repository.FinanzierungRepository;
import de.muenchen.stadtbezirksbudget.backend.antrag.repository.ProjektRepository;
import de.muenchen.stadtbezirksbudget.backend.antrag.repository.ZahlungsempfaengerRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public record AntragsdatenTestDataBuilder(
        AntragRepository antragRepository,
        AdresseRepository adresseRepository,
        FinanzierungRepository finanzierungRepository,
        ZahlungsempfaengerRepository antragstellerRepository,
        ProjektRepository projektRepository,
        BearbeitungsstandRepository bearbeitungsstandRepository,
        BankverbindungRepository bankverbindungRepository) {

    private static String generateRandomUuidString() {
        return UUID.randomUUID().toString();
    }

    private Adresse initializeAdresse() {
        final Adresse adresse = new Adresse();
        adresse.setStrasse("Musterstraße " + generateRandomUuidString());
        adresse.setHausnummer("1");
        adresse.setPostleitzahl("12345");
        adresse.setOrt("München");
        return adresseRepository.save(adresse);
    }

    private Antragsteller initializeAntragsteller(final Adresse adresse) {
        final Antragsteller antragsteller = new Antragsteller();
        antragsteller.setName("Max Mustermann " + generateRandomUuidString());
        antragsteller.setZielsetzung("Förderung von Projekten " + generateRandomUuidString());
        antragsteller.setRechtsform(Rechtsform.NATUERLICHE_PERSON);
        antragsteller.setTelefonNr("0123456789");
        antragsteller.setEmail("max_" + generateRandomUuidString() + "@mustermann.de");
        antragsteller.setAdresse(adresse);
        return antragstellerRepository.save(antragsteller);
    }

    private Projekt initializeProjekt() {
        final Projekt projekt = new Projekt();
        projekt.setTitel("Projekt XYZ " + generateRandomUuidString());
        projekt.setBeschreibung("Beschreibung des Projekts " + generateRandomUuidString());
        projekt.setStart(LocalDate.now());
        projekt.setEnde(LocalDate.now().plusMonths(6));
        return projektRepository.save(projekt);
    }

    private Bearbeitungsstand initializeBearbeitungsstand() {
        final Bearbeitungsstand bearbeitungsstand = new Bearbeitungsstand();
        bearbeitungsstand.setAnmerkungen("Antrag in Bearbeitung " + generateRandomUuidString());
        bearbeitungsstand.setIstMittelabruf(false);
        bearbeitungsstand.setStatus(Status.VOLLSTAENDIG);
        return bearbeitungsstandRepository.save(bearbeitungsstand);
    }

    private Finanzierung initializeFinanzierung() {
        final Finanzierung finanzierung = new Finanzierung();
        finanzierung.setIstProjektVorsteuerabzugsberechtigt(true);
        finanzierung.setSonstigeFoerderhinweise("Keine");
        finanzierung.setBewilligterZuschuss(10_000.0);

        final Finanzierungsmittel finanzierungsmittel = new Finanzierungsmittel();
        finanzierungsmittel.setKategorie(Kategorie.EIGENMITTEL);
        finanzierungsmittel.setBetrag(2000.0);
        finanzierungsmittel.setDirektoriumNotiz("Notiz zu Finanzierungsmitteln " + generateRandomUuidString());
        finanzierungsmittel.setFinanzierung(finanzierung);

        final List<Finanzierungsmittel> finanzierungsmittelListe = new ArrayList<>();
        finanzierungsmittelListe.add(finanzierungsmittel);
        finanzierung.setFinanzierungsmittelListe(finanzierungsmittelListe);

        final VoraussichtlicheAusgabe ausgabe = new VoraussichtlicheAusgabe();
        ausgabe.setKategorie("Material " + generateRandomUuidString());
        ausgabe.setBetrag(5000.0);
        ausgabe.setDirektoriumNotiz("Notiz zu Materialausgaben " + generateRandomUuidString());
        ausgabe.setFinanzierung(finanzierung);

        final List<VoraussichtlicheAusgabe> voraussichtlicheAusgabenListe = new ArrayList<>();
        voraussichtlicheAusgabenListe.add(ausgabe);
        finanzierung.setVoraussichtlicheAusgaben(voraussichtlicheAusgabenListe);

        return finanzierungRepository.save(finanzierung);
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

    public Antrag initializeAntrag() {
        final Antrag antrag = new Antrag();
        final Adresse adresse = initializeAdresse();
        final Antragsteller antragsteller = initializeAntragsteller(adresse);
        antrag.setEingangsdatum(LocalDate.now());
        antrag.setBezirksausschussNr(1);
        antrag.setIstPersonVorsteuerabzugsberechtigt(true);
        antrag.setIstAndererZuwendungsantrag(false);
        antrag.setBearbeitungsstand(initializeBearbeitungsstand());
        antrag.setFinanzierung(initializeFinanzierung());
        antrag.setProjekt(initializeProjekt());
        antrag.setAntragsteller(antragsteller);
        antrag.setBankverbindung(initializeBankverbindung(antragsteller));
        return antragRepository.save(antrag);
    }
}
