// Array of all rechtsform options
export const rechtsformOptions = [
  {
    value: "NATUERLICHE_PERSON",
  },
  {
    value: "JURISTISCHE_PERSON",
  },
  {
    value: "SONSTIGE_VEREINIGUNGEN",
  },
] as const satisfies readonly RechtsformOption[];

// Type representing all possible rechtsform values
export type Rechtsform = (typeof rechtsformOptions)[number]["value"];

// Interface for rechtsform options
export interface RechtsformOption {
  value: string;
}
