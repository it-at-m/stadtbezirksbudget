package de.muenchen.stadtbezirksbudget.antrag.entities;

public enum Status {
    eingegangen,
    wartenAufBuergerrueckmeldung,
    abgelehnt_keineRueckmeldung,
    abgelehnt_nichtZustaendig,
    abgelehnt_nichtFoerderfaehig,
    vollstaendig,
    sitzungsvorlageErstellt,
    sitzungsvorlageGenehmigt,
    bereitZurAbstimmung,
    abgelehnt_vonBA,
    antragAngenommen,
    bescheidVerfuegen,
    mitteilungAnBuerger,
    pruefungRechnungen,
    auszahlung,
    rueckforderung,
    rueckzahlung,
    abgeschlossen
}
