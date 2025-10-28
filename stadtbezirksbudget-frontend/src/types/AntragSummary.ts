import { Status } from "./Status.ts";

export default interface AntragSummary {
  id: string;
  status: Status;
  zammadNr: string;
  bezirksausschussNr: number;
  eingangDatum: Date;
  projektTitel: string;
  antragstellerName: string;
  beantragtesBudget: number;
  aktualisierung: string;
  aktualisierungDatum: Date;
  anmerkungen: string;
  bearbeiter: string;
}
