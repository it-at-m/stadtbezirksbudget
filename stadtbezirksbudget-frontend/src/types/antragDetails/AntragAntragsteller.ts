import type { AntragAdresse } from "@/types/antragDetails/AntragAdresse.ts";
import type { Rechtsform } from "@/types/antragDetails/enums/Rechtsform.ts";

export interface AntragAntragsteller {
  vorname: string;
  name: string;
  telefonNr: string;
  email: string;
  zielsetzung: string;
  rechtsform: Rechtsform;
  adresse: AntragAdresse;
}
