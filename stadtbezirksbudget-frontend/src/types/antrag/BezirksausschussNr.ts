const COUNT_BEZIRKE = 25 as const;

const bezirksausschussNrDefinitions = Array.from(
  { length: COUNT_BEZIRKE },
  (_, i) => ({
    value: i + 1,
    title: `BA ${i + 1}`,
  })
);

// Array of all bezirksausschuss nr options
export const bezirksausschussNrOptions: readonly BezirksausschussNrOption[] = [
  ...bezirksausschussNrDefinitions,
] as const;

// Interface for bezirksausschuss nr options
export interface BezirksausschussNrOption {
  value: number;
  title: string;
}
