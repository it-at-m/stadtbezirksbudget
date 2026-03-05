import type { AntragAllgemein } from "@/types/antragDetails/AntragAllgemein.ts";
import type { AntragAntragsteller } from "@/types/antragDetails/AntragAntragsteller.ts";
import type { AntragBankverbindung } from "@/types/antragDetails/AntragBankverbindung.ts";
import type { AntragBezirksinformationen } from "@/types/antragDetails/AntragBezirksinformationen.ts";
import type { AntragFinanzierung } from "@/types/antragDetails/AntragFinanzierung.ts";
import type { AntragProjekt } from "@/types/antragDetails/AntragProjekt.ts";
import type { AntragVertretungsberechtigter } from "@/types/antragDetails/AntragVertretungsberechtigter.ts";
import type { AntragVerwendungsnachweis } from "@/types/antragDetails/AntragVerwendungsnachweis.ts";
import type { AntragZahlung } from "@/types/antragDetails/AntragZahlung.ts";

export interface AntragDetails {
  allgemein: AntragAllgemein;
  antragsteller: AntragAntragsteller;
  bankverbindung: AntragBankverbindung;
  bezirksinformationen: AntragBezirksinformationen;
  finanzierung: AntragFinanzierung;
  projekt: AntragProjekt;
  vertretungsberechtigter?: AntragVertretungsberechtigter;
  verwendungsnachweis: AntragVerwendungsnachweis;
  zahlung: AntragZahlung;
}
