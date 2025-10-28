import { Status } from "./Status.ts";

export default interface AntragSummary {
  id: string;
  antragsstatus: Status;
  zammadNummer: string;
  bezirksausschussnummer: number;
  eingangsdatum: Date;
  projekttitel: string;
  antragstellerName: string;
  beantragtesBudget: number;
  aktualisierungsArt: string;
  datumAktualisierung: Date;
  anmerkungen: string;
  bearbeiter: string;
}
