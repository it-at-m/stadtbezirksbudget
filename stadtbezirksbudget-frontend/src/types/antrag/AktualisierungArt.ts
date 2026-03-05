// Array of all aktualisierung art options
export const aktualisierungArtOptions = [
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
] as const satisfies readonly AktualisierungArtOption[];

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
  aktualisierungArtOptions.reduce(
    (acc, a) => {
      acc[a.value as AktualisierungArt] = a.title;
      return acc;
    },
    {} as Record<AktualisierungArt, string>
  );
