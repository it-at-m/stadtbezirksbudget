import { Status } from "./Status.ts";

export default interface AntragSummary {
  id: string;
  antragsstatus: Status;
  bezirksausschussnummer: number;
  eingangsdatum: Date;
  projekttitel: string;
  antragstellerName: string;
  beantragtesBudget: number;
  anmerkungen: string;
  bearbeiter: string;
}
