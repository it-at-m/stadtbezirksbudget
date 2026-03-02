// Array of all FinanzierungsmittelKategorie options
export const finanzierungsmittelKategorieOptions: readonly FinanzierungsmittelKategorieOption[] =
  [
    {
      value: "EINNAHMEN",
    },
    {
      value: "EIGENMITTEL",
    },
    {
      value: "ZUWENDUNGEN_DRITTER",
    },
  ] as const;

// Type representing all possible FinanzierungsmittelKategorie values
export type FinanzierungsmittelKategorie =
  (typeof finanzierungsmittelKategorieOptions)[number]["value"];

// Interface for FinanzierungsmittelKategorie options
export interface FinanzierungsmittelKategorieOption {
  value: string;
}
