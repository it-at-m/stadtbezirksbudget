// Array of all finanzierung art options
export const finanzierungArtOptions: readonly FinanzierungArtOption[] = [
  {
    value: "FEHL",
    title: "Fehl",
  },
  {
    value: "FEST",
    title: "Fest",
  },
] as const;

// Type representing all possible finanzierung art values
export type FinanzierungArt = (typeof finanzierungArtOptions)[number]["value"];

// Interface for finanzierung art options
export interface FinanzierungArtOption {
  value: string;
  title: string;
}

// Mapping of finanzierung art values to their corresponding short and long texts
export const FinanzierungArtText: Record<FinanzierungArt, string> =
  Object.fromEntries(finanzierungArtOptions.map((a) => [a.value, a.title]));
