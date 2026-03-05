import type { AntragAdresse } from "@/types/antragDetails/AntragAdresse.ts";

export interface AntragVertretungsberechtigter {
  nachname: string;
  vorname: string;
  adresse: AntragAdresse;
  email: string;
  telefonNr: string;
  mobilNr: string;
}
