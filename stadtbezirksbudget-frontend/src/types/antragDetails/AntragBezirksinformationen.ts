export interface AntragBezirksinformationen {
  ausschussNr: number;
  sitzungDatum: string; // ISO date string from API
  risNr?: string;
  bewilligteFoerderung?: number;
  bescheidDatum?: string; // ISO date string from API
}
