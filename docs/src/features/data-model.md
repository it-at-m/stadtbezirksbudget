# Data model

```mermaid
classDiagram
    Antrag "1..n"--"1" Antragssteller
    Antrag "1..n"--"1" Projekt
    Antrag "1"--"1" Finanzierung
    Antrag "1" -- "1" Bearbeitungsstand
    Antrag "1..n"--"1" Bankverbindung
    Antrag "1..n"--"0..1" Vertretungsberechtigter
    Antrag "1" -- "0..n" AndererZuwendungsantrag
    Bearbeitungsstand "1"--"1" Status
    Finanzierung "1"--"1..n" VoraussichtlicheAusgabe
    Finanzierung "1"--"1..n" Finanzierungsmittel
    Adresse "1"--"1..n" Zahlungsempfaenger
    Zahlungsempfaenger "1"--"1..n" Bankverbindung
    Zahlungsempfaenger <|-- Vertretungsberechtigter
    Zahlungsempfaenger <|-- Antragssteller

    class Antrag{
        eingangsdatum: Date
        istPersonVorsteuerabzugsberechtigt: boolean
        bezirksausschussNr: int
    }

    class Bearbeitungsstand{
        anmerkungen: String
        istMittelabruf: boolean
    }

    class AndererZuwendungsantrag{
        antragsdatum: Date
        stelle: String
    }

    class Projekt{
        beschreibung: String<fk>
        ende: Date<fk>
        start: Date<fk>
        titel: String<fk>
    %% All attributes except UUID are <fk>
    }

    class Bankverbindung{
        bic: String<fk>
        geldinstitut: String<fk>
        iban: String<fk>
        person: String<fk>
    %% All attributes except UUID are <fk>
    }

    class Finanzierung{
        bewilligterZuschuss: [Optional] Double
        istEinladungsFoerderhinweis: boolean
        istProjektVorsteuerabzugsberechtigt: boolean
        istWebsiteFoerderhinweis: boolean
        sonstigerFoerderhinweis: String
    }

    class Antragssteller{
        name: String<fk>
        rechtsform: Enum[natuerlichPerson, juristischePerson, sonstigeVereinigungen]<fk>
        zielsetzung: String<fk>
    %% All attributes except UUID are <fk>
    }

    class Adresse{
        hausnummer: String<fk>
        ort: String<fk>
        postleitzahl: String<fk>
        strasse: String<fk>
    %% All attributes except UUID are <fk>
    }

    class Vertretungsberechtigter{
        mobilNr: String<fk>
        nachname: String<fk>
        vorname: String<fk>
    %% All attributes except UUID are <fk>
    }

    class VoraussichtlicheAusgabe{
        betrag: double
        direktoriumNotiz: String
        kategorie: String
    }

    class Finanzierungsmittel{
        betrag: double
        direktoriumNotiz: String
        kategorie: Enum[einnahmen, eigenmittel, zuwendungenDritter]
    }

    class Zahlungsempfaenger{
        <<abstract>>
        email: String<fk>
        telefonNr: String<fk>
    %% All attributes except UUID are <fk> (including child-attributes)
    }

    class Status {
        <<enumeration>>
        eingegangen
        wartenAufBuergerrueckmeldung
        abgelehnt_keineRueckmeldung
        abgelehnt_nichtZustaendig
        abgelehnt_nichtFoerderfaehig
        vollstaendig
        sitzungsvorlageErstellt
        sitzungsvorlageGenehmigt
        bereitZurAbstimmung
        abgelehnt_vonBA
        antragAngenommen
        bescheidVerfuegen
        mitteilungAnBuerger
        pruefungRechnungen
        auszahlung
        rueckforderung
        rueckzahlung
        abgeschlossen
    }
```
