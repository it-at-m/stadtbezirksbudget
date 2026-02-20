// Array of all Bezirksausschuss options
export const bezirksausschussOptions = [
  { value: 1, title: "Altstadt-Lehel" },
  { value: 2, title: "Ludwigsvorstadt-Isarvorstadt" },
  { value: 3, title: "Maxvorstadt" },
  { value: 4, title: "Schwabing-West" },
  { value: 5, title: "Au-Haidhausen" },
  { value: 6, title: "Sendling" },
  { value: 7, title: "Sendling-Westpark" },
  { value: 8, title: "Schwanthalerhöhe" },
  { value: 9, title: "Neuhausen-Nymphenburg" },
  { value: 10, title: "Moosach" },
  { value: 11, title: "Milbertshofen-Am Hart" },
  { value: 12, title: "Schwabing-Freimann" },
  { value: 13, title: "Bogenhausen" },
  { value: 14, title: "Berg am Laim" },
  { value: 15, title: "Trudering-Riem" },
  { value: 16, title: "Ramersdorf-Perlach" },
  { value: 17, title: "Obergiesing-Fasangarten" },
  { value: 18, title: "Untergiesing-Harlaching" },
  {
    value: 19,
    title: "Thalkirchen-Obersendling-Forstenried-Fürstenried-Solln",
  },
  { value: 20, title: "Hadern" },
  { value: 21, title: "Pasing-Obermenzing" },
  { value: 22, title: "Aubing-Lochhausen-Langwied" },
  { value: 23, title: "Allach-Untermenzing" },
  { value: 24, title: "Feldmoching-Hasenbergl" },
  { value: 25, title: "Laim" },
] as const;

// Type representing all possible Bezirksausschuss values
export type Bezirksausschuss =
  (typeof bezirksausschussOptions)[number]["value"];

// Interface for Bezirksausschuss options
export interface BezirksausschussOption {
  value: number;
  title: string;
}

// Mapping of Bezirksausschuss values to their corresponding titles
export const BezirksausschussText: Record<Bezirksausschuss, string> =
  Object.fromEntries(
    bezirksausschussOptions.map((option) => [option.value, option.title])
  ) as Record<Bezirksausschuss, string>;

// Function to get Bezirksname by number
export function getBezirksnameByNumber(bezirksausschussNr: number): string {
  const bezirk = bezirksausschussOptions.find(
    (option) => option.value === bezirksausschussNr
  );
  if (bezirk) {
    return bezirk.title;
  }
  throw new Error(`Invalid Bezirksausschuss number: ${bezirksausschussNr}`);
}
