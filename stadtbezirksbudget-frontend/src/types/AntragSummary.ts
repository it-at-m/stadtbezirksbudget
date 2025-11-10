import type { AktualisierungsArt } from "@/types/AktualisierungsArt.ts";

import { Status } from "./Status.ts";

export default interface AntragSummary {
  id: string;
  status: Status;
  zammadNr: string;
  aktenzeichen: string;
  bezirksausschussNr: number;
  eingangDatum: string; // ISO date string from API
  antragstellerName: string;
  projektTitel: string;
  beantragtesBudget: number;
  istFehlbetrag: boolean;
  aktualisierung: AktualisierungsArt;
  aktualisierungDatum: string; // ISO date string from API
}
