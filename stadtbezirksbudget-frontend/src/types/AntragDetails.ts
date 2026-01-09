import type { Status } from "@/types/Status.ts";

export interface AntragDetails {
  projektTitel: string;
  eingangDatum: string; // ISO date string from API
  antragstellerName: string;
  beantragtesBudget: number;
  rubrik: string;
  status: Status;
  zammadNr: string;
  aktenzeichen: string;
  istGegendert: boolean;
  anmerkungen: string;
}
