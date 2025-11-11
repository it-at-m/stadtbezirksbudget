export enum Status {
  EINGEGANGEN = "EINGEGANGEN",
  WARTEN_AUF_BUERGERRUECKMELDUNG = "WARTEN_AUF_BUERGERRUECKMELDUNG",
  ABGELEHNT_KEINE_RUECKMELDUNG = "ABGELEHNT_KEINE_RUECKMELDUNG",
  ABGELEHNT_NICHT_ZUSTAENDIG = "ABGELEHNT_NICHT_ZUSTAENDIG",
  ABGELEHNT_NICHT_FOERDERFAEHIG = "ABGELEHNT_NICHT_FOERDERFAEHIG",
  ABGELEHNT_VON_BA = "ABGELEHNT_VON_BA",
  ABLEHNUNGSBESCHEID_ERSTELLT = "ABLEHNUNGSBESCHEID_ERSTELLT",
  VOLLSTAENDIG = "VOLLSTAENDIG",
  SITZUNGSVORLAGE_ERSTELLT = "SITZUNGSVORLAGE_ERSTELLT",
  SITZUNGSVORLAGE_UEBERMITTELT = "SITZUNGSVORLAGE_UEBERMITTELT",
  BESCHLUSS_ERHALTEN = "BESCHLUSS_ERHALTEN",
  ABLEHNUNGSBESCHLUSS_ERHALTEN = "ABLEHNUNGSBESCHLUSS_ERHALTEN",
  ZUWENDUNGSBESCHEID_ERSTELLT = "ZUWENDUNGSBESCHEID_ERSTELLT",
  ZUWENDUNGSBESCHEID_VERSENDET = "ZUWENDUNGSBESCHEID_VERSENDET",
  VERWENDUNGSNACHWEISE_GEPRUEFT = "VERWENDUNGSNACHWEISE_GEPRUEFT",
  AUSZAHLUNG = "AUSZAHLUNG",
  RUECKZAHLUNG = "RUECKZAHLUNG",
  ABGESCHLOSSEN = "ABGESCHLOSSEN",
}

export const StatusText: Record<Status, Omit<StatusOption, "value">> = {
  [Status.EINGEGANGEN]: {
    shortText: "Offen",
    longText: "Antrag eingegangen",
  },
  [Status.WARTEN_AUF_BUERGERRUECKMELDUNG]: {
    shortText: "Warten",
    longText: "Warten auf Bürgerrückmeldung",
  },
  [Status.ABGELEHNT_KEINE_RUECKMELDUNG]: {
    shortText: "Abgelehnt",
    longText: "Abgelehnt - Keine Rückmeldung",
  },
  [Status.ABGELEHNT_NICHT_ZUSTAENDIG]: {
    shortText: "Abgelehnt",
    longText: "Abgelehnt - Nicht zuständig",
  },
  [Status.ABGELEHNT_NICHT_FOERDERFAEHIG]: {
    shortText: "Abgelehnt",
    longText: "Abgelehnt - Nicht förderfähig",
  },
  [Status.ABGELEHNT_VON_BA]: {
    shortText: "Abgelehnt",
    longText: "Abgelehnt von BA (Bescheid wurde versendet)",
  },
  [Status.ABLEHNUNGSBESCHEID_ERSTELLT]: {
    shortText: "Zurückgewiesen",
    longText: "Ablehnungsbescheid erstellt",
  },
  [Status.VOLLSTAENDIG]: {
    shortText: "Vorbereitung",
    longText: "Antrag vollständig",
  },
  [Status.SITZUNGSVORLAGE_ERSTELLT]: {
    shortText: "Vorbereitung",
    longText: "Sitzungsvorlage erstellt",
  },
  [Status.SITZUNGSVORLAGE_UEBERMITTELT]: {
    shortText: "Vorbereitung",
    longText: "Sitzungsvorlage an BA/Geschäftsstelle übermittelt",
  },
  [Status.BESCHLUSS_ERHALTEN]: {
    shortText: "Angenommen",
    longText: "Beschluss Erhalten",
  },
  [Status.ABLEHNUNGSBESCHLUSS_ERHALTEN]: {
    shortText: "Zurückgewiesen",
    longText: "Ablehnungsbeschluss erhalten",
  },
  [Status.ZUWENDUNGSBESCHEID_ERSTELLT]: {
    shortText: "Bewilligt",
    longText: "Zuwendungsbescheid erstellt",
  },
  [Status.ZUWENDUNGSBESCHEID_VERSENDET]: {
    shortText: "Nachricht an Bürger",
    longText: "Zuwendungsbescheid versendet",
  },
  [Status.VERWENDUNGSNACHWEISE_GEPRUEFT]: {
    shortText: "Finanzierung",
    longText: "Verwendungsnachweise geprüft",
  },
  [Status.AUSZAHLUNG]: {
    shortText: "Finanzierung",
    longText: "An die Stelle ausgezahlt",
  },
  [Status.RUECKZAHLUNG]: {
    shortText: "Rückzahlung",
    longText: "Rückzahlung angefordert",
  },
  [Status.ABGESCHLOSSEN]: {
    shortText: "Abgeschlossen",
    longText: "Abrechnungsschreiben erstellt",
  },
};

export interface StatusOption {
  value: Status;
  shortText: string;
  longText: string;
}
