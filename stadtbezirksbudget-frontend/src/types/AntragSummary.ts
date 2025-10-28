import { Status } from "./Status.ts";

export default interface AntragSummary {
  id: string;
  antragsstatus: Status;
  zammadNummer: string;
  bezirksausschussnummer: number;
  eingangDatum: Date;
  projekttitel: string;
  antragstellerName: string;
  beantragtesBudget: number;
  aktualisierung: string;
  aktualisierungDatum: Date;
  anmerkungen: string;
  bearbeiter: string;
}
