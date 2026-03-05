// Array of all BeschlussStatus options
export const beschlussStatusOptions = [
  {
    value: "BEWILLIGT",
  },
  {
    value: "TEILBEWILLIGT",
  },
  {
    value: "ABGELEHNT",
  },
  {
    value: "LEER",
  },
] as const satisfies readonly BeschlussStatusOption[];

// Type representing all possible BeschlussStatus values
export type BeschlussStatus = (typeof beschlussStatusOptions)[number]["value"];

// Interface for BeschlussStatus options
export interface BeschlussStatusOption {
  value: string;
}
