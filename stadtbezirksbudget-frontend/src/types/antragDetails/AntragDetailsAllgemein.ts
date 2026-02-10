import type { Status } from "@/types/Status.ts";

export interface AntragDetailsAllgemein {
  projektTitel: string;
  eingangDatum: string; // ISO date string from API
  antragstellerName: string;
  beantragtesBudget: number;
  rubrik: string;
  status: Status;
  zammadNr: string;
  aktenzeichen: string;
  eakteCooAdresse: string;
  istGegendert: boolean;
  anmerkungen: string;
}
