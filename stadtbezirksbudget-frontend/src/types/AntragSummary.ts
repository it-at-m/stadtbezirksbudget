import { Status } from "./Status.ts";

export default interface AntragSummary {
  id: string;
  status: Status;
  zammadNr: string;
  bezirksausschussNr: number;
  eingangDatum: string; // ISO date string from API
  projektTitel: string;
  antragstellerName: string;
  beantragtesBudget: number;
  aktualisierung: string;
  aktualisierungDatum: string; // ISO date string from API
  anmerkungen: string;
  bearbeiter: string;
}
