// Array of all aktualisierung art options
export const aktualisierungArtOptions: readonly AktualisierungArtOption[] = [
  {
    value: "E_AKTE",
    shortText: "E-Akte",
    longText: "Aktualisierung E-Akte",
  },
  {
    value: "ZAMMAD",
    shortText: "Zammad",
    longText: "Aktualisierung in Zammad",
  },
  {
    value: "FACHANWENDUNG",
    shortText: "Fachanwendung",
    longText: "Aktualisierung Datensatz",
  },
] as const;

// Type representing all possible aktualisierung art values
export type AktualisierungArt =
  (typeof aktualisierungArtOptions)[number]["value"];

// Interface for aktualisierung art options
export interface AktualisierungArtOption {
  value: string;
  shortText: string;
  longText: string;
}

// Mapping of aktualisierung art values to their corresponding short and long texts
export const AktualisierungArtText: Record<
  AktualisierungArt,
  Omit<AktualisierungArtOption, "value">
> = Object.fromEntries(
  aktualisierungArtOptions.map((a) => [
    a.value,
    { shortText: a.shortText, longText: a.longText },
  ])
);
