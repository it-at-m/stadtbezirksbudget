// Array of all rechtsform options
export const rechtsformOptions: readonly RechtsformOption[] = [
  {
    value: "NATUERLICHE_PERSON",
  },
  {
    value: "JURISTISCHE_PERSON",
  },
  {
    value: "SONSTIGE_VEREINIGUNGEN",
  },
] as const;

// Type representing all possible rechtsform values
export type Rechtsform = (typeof rechtsformOptions)[number]["value"];

// Interface for rechtsform options
export interface RechtsformOption {
  value: string;
}
