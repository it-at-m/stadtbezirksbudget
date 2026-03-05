// Array of all AndererZuwendungsantragStatus options
export const andererZuwendungsantragStatusOptions = [
  {
    value: "BEANTRAGT",
  },
  {
    value: "ZUGESAGT",
  },
] as const satisfies readonly AndererZuwendungsantragStatusOption[];

// Type representing all possible AndererZuwendungsantragStatus values
export type AndererZuwendungsantragStatus =
  (typeof andererZuwendungsantragStatusOptions)[number]["value"];

// Interface for AndererZuwendungsantragStatus options
export interface AndererZuwendungsantragStatusOption {
  value: string;
}
