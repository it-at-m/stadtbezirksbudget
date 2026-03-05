// Array of all finanzierung art options
export const finanzierungArtOptions = [
  {
    value: "FEHL",
    title: "Fehl",
  },
  {
    value: "FEST",
    title: "Fest",
  },
] as const satisfies readonly FinanzierungArtOption[];

// Type representing all possible finanzierung art values
export type FinanzierungArt = (typeof finanzierungArtOptions)[number]["value"];

// Interface for finanzierung art options
export interface FinanzierungArtOption {
  value: string;
  title: string;
}

// Mapping of finanzierung art values to their corresponding short and long texts
export const FinanzierungArtText: Record<FinanzierungArt, string> =
  finanzierungArtOptions.reduce(
    (acc, a) => {
      acc[a.value as FinanzierungArt] = a.title;
      return acc;
    },
    {} as Record<FinanzierungArt, string>
  );
