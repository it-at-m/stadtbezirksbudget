import type { AktualisierungArt } from "@/types/antrag/AktualisierungArt.ts";
import type { FinanzierungArt } from "@/types/antrag/FinanzierungArt.ts";
import type { Status } from "@/types/antrag/Status.ts";

export default interface AntragSummary {
  id: string;
  status: Status;
  zammadNr: string;
  aktenzeichen: string;
  eakteCooAdresse: string;
  bezirksausschussNr: number;
  eingangDatum: string; // ISO date string from API
  antragstellerName: string;
  projektTitel: string;
  beantragtesBudget: number;
  finanzierungArt: FinanzierungArt;
  aktualisierung: AktualisierungArt;
  aktualisierungDatum: string; // ISO date string from API
}
