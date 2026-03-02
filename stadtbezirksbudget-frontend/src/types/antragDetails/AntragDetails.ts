import type { AntragAllgemein } from "@/types/antragDetails/AntragAllgemein.ts";
import type { AntragAntragsteller } from "@/types/antragDetails/AntragAntragsteller.ts";
import type { AntragFinanzierung } from "@/types/antragDetails/AntragFinanzierung.ts";
import type { AntragProjekt } from "@/types/antragDetails/AntragProjekt.ts";

export interface AntragDetails {
  allgemein: AntragAllgemein;
  antragsteller: AntragAntragsteller;
  finanzierung: AntragFinanzierung;
  projekt: AntragProjekt;
}
