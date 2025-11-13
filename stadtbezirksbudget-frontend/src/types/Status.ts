const STATUS_DEFINITIONS = [
  { value: "EINGEGANGEN", shortText: "Offen", longText: "Antrag eingegangen" },
  {
    value: "WARTEN_AUF_BUERGERRUECKMELDUNG",
    shortText: "Warten",
    longText: "Warten auf Bürgerrückmeldung",
  },
  {
    value: "VOLLSTAENDIG",
    shortText: "Vorbereitung",
    longText: "Antrag vollständig",
  },
  {
    value: "SITZUNGSVORLAGE_ERSTELLT",
    shortText: "Vorbereitung",
    longText: "Sitzungsvorlage erstellt",
  },
  {
    value: "SITZUNGSVORLAGE_UEBERMITTELT",
    shortText: "Vorbereitung",
    longText: "Sitzungsvorlage an BA/Geschäftsstelle übermittelt",
  },
  {
    value: "BESCHLUSS_ERHALTEN",
    shortText: "Angenommen",
    longText: "Beschluss Erhalten",
  },
  {
    value: "ZUWENDUNGSBESCHEID_ERSTELLT",
    shortText: "Bewilligt",
    longText: "Zuwendungsbescheid erstellt",
  },
  {
    value: "ZUWENDUNGSBESCHEID_VERSENDET",
    shortText: "Nachricht an Bürger",
    longText: "Zuwendungsbescheid versendet",
  },
  {
    value: "VERWENDUNGSNACHWEISE_GEPRUEFT",
    shortText: "Finanzierung",
    longText: "Verwendungsnachweise geprüft",
  },
  {
    value: "AUSZAHLUNG",
    shortText: "Finanzierung",
    longText: "An die Stelle ausgezahlt",
  },
  {
    value: "RUECKZAHLUNG",
    shortText: "Rückzahlung",
    longText: "Rückzahlung angefordert",
  },
  {
    value: "ABGESCHLOSSEN",
    shortText: "Abgeschlossen",
    longText: "Abrechnungsschreiben erstellt",
  },
  {
    value: "ABLEHNUNGSBESCHEID_ERSTELLT",
    shortText: "Zurückgewiesen",
    longText: "Ablehnungsbescheid erstellt",
  },
  {
    value: "ABLEHNUNGSBESCHLUSS_ERHALTEN",
    shortText: "Zurückgewiesen",
    longText: "Ablehnungsbeschluss erhalten",
  },
  {
    value: "ABGELEHNT_VON_BA",
    shortText: "Abgelehnt",
    longText: "Abgelehnt von BA (Bescheid wurde versendet)",
  },
  {
    value: "ABGELEHNT_KEINE_RUECKMELDUNG",
    shortText: "Abgelehnt",
    longText: "Abgelehnt - Keine Rückmeldung",
  },
  {
    value: "ABGELEHNT_NICHT_ZUSTAENDIG",
    shortText: "Abgelehnt",
    longText: "Abgelehnt - Nicht zuständig",
  },
  {
    value: "ABGELEHNT_NICHT_FOERDERFAEHIG",
    shortText: "Abgelehnt",
    longText: "Abgelehnt - Nicht förderfähig",
  },
] as const;

export type Status = (typeof STATUS_DEFINITIONS)[number]["value"];

export interface StatusOption {
  value: Status;
  shortText: string;
  longText: string;
}

export const StatusText: Record<
  Status,
  Omit<StatusOption, "value">
> = Object.fromEntries(
  STATUS_DEFINITIONS.map((s) => [
    s.value,
    { shortText: s.shortText, longText: s.longText },
  ])
) as Record<Status, Omit<StatusOption, "value">>;

export const statusOptions: StatusOption[] = STATUS_DEFINITIONS.map((s) => ({
  value: s.value,
  shortText: s.shortText,
  longText: s.longText,
}));
