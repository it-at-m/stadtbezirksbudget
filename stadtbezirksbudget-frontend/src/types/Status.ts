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
    longText: "Beschluss erhalten",
  },
  {
    value: "ZUWENDUNGSBESCHEID_ERSTELLT",
    shortText: "Bewilligt",
    longText: "Zuwendungsbescheid erstellt",
  },
  {
    value: "ZUWENDUNGSBESCHEID_VERSENDET",
    shortText: "Bewilligt",
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
    longText: "An Antragsteller ausgezahlt",
  },
  {
    value: "ABRECHNUNGSSCHREIBEN_ERSTELLT",
    shortText: "Finanzierung",
    longText: "Abrechnungsschreiben erstellt",
  },
  {
    value: "RUECKZAHLUNG",
    shortText: "Rückzahlung",
    longText: "Rückzahlung angefordert",
  },
  {
    value: "ABGESCHLOSSEN",
    shortText: "Abgeschlossen",
    longText: "Vorgang Abgeschlossen",
  },
  {
    value: "ABLEHNUNGSBESCHLUSS_ERHALTEN",
    shortText: "Zurückgewiesen",
    longText: "Ablehnungsbeschluss erhalten",
  },
  {
    value: "ABLEHNUNGSBESCHEID_ERSTELLT",
    shortText: "Zurückgewiesen",
    longText: "Ablehnungsbescheid erstellt",
  },
  {
    value: "ABGELEHNT_VON_BA",
    shortText: "Abgelehnt",
    longText: "Abgelehnt von BA (Bescheid wurde versendet)",
  },
  {
    value: "ABGELEHNT_KEINE_RUECKMELDUNG",
    shortText: "Abgelehnt",
    longText: "Abgelehnt - Fehlende Mitwirkung",
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

// Type representing all possible status values
export type Status = (typeof STATUS_DEFINITIONS)[number]["value"];

// Interface for status options used in dropdowns or selectors
export interface StatusOption {
  value: Status;
  shortText: string;
  longText: string;
}

// Mapping of status values to their corresponding short and long texts
export const StatusText: Record<
  Status,
  Omit<StatusOption, "value">
> = Object.fromEntries(
  STATUS_DEFINITIONS.map((s) => [
    s.value,
    { shortText: s.shortText, longText: s.longText },
  ])
) as Record<Status, Omit<StatusOption, "value">>;

// Array of all status options for use in UI components
export const statusOptions: readonly StatusOption[] = STATUS_DEFINITIONS;
