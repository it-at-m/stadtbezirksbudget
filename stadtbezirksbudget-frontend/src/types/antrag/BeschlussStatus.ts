// Array of all BeschlussStatus options
export const beschlussStatusOptions: readonly BeschlussStatusOption[] = [
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
] as const;

// Type representing all possible BeschlussStatus values
export type BeschlussStatus = (typeof beschlussStatusOptions)[number]["value"];

// Interface for BeschlussStatus options
export interface BeschlussStatusOption {
  value: string;
}
