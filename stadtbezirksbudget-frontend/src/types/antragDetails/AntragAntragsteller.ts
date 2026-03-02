import type { Rechtsform } from "@/types/antrag/Rechtsform.ts";
import type { AntragAdresse } from "@/types/antragDetails/AntragAdresse.ts";

export interface AntragAntragsteller {
  vorname: string;
  name: string;
  telefonNr: string;
  email: string;
  zielsetzung: string;
  rechtsform: Rechtsform;
  adresse: AntragAdresse;
}
