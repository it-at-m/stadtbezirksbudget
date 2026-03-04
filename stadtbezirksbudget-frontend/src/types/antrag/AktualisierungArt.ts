// Array of all aktualisierung art options
export const aktualisierungArtOptions: readonly AktualisierungArtOption[] = [
  {
    value: "E_AKTE",
    title: "E-Akte",
  },
  {
    value: "ZAMMAD",
    title: "Zammad",
  },
  {
    value: "FACHANWENDUNG",
    title: "Fachanwendung",
  },
] as const;

// Type representing all possible aktualisierung art values
export type AktualisierungArt =
  (typeof aktualisierungArtOptions)[number]["value"];

// Interface for aktualisierung art options
export interface AktualisierungArtOption {
  value: string;
  title: string;
}

// Mapping of aktualisierung art values to their corresponding short and long texts
export const AktualisierungArtText: Record<AktualisierungArt, string> =
  Object.fromEntries(aktualisierungArtOptions.map((a) => [a.value, a.title]));
