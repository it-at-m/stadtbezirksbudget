import type { AktualisierungArt } from "@/types/AktualisierungArt.ts";
import type { Status } from "@/types/Status.ts";

// Interface for filtering Antrag list
export interface AntragListFilter {
  status: Status[];
  bezirksausschussNr: number[];
  eingangDatumVon?: Date;
  eingangDatumBis?: Date;
  antragstellerName?: string;
  projektTitel?: string;
  beantragtesBudgetVon?: number;
  beantragtesBudgetBis?: number;
  art?: string;
  aktualisierungArt: AktualisierungArt[];
  aktualisierungDatumVon?: Date;
  aktualisierungDatumBis?: Date;
}

// Creates an AntragListFilter object with default values
export const defaultAntragListFilter = (): AntragListFilter => ({
  ...emptyAntragListFilter(),
  status: [
    "EINGEGANGEN",
    "VOLLSTAENDIG",
    "SITZUNGSVORLAGE_ERSTELLT",
    "SITZUNGSVORLAGE_UEBERMITTELT",
    "WARTEN_AUF_BUERGERRUECKMELDUNG",
    "BESCHLUSS_ERHALTEN",
    "ZUWENDUNGSBESCHEID_ERSTELLT",
    "ZUWENDUNGSBESCHEID_VERSENDET",
  ],
});

// Creates an empty AntragListFilter object
export const emptyAntragListFilter = (): AntragListFilter => ({
  status: [],
  bezirksausschussNr: [],
  eingangDatumVon: undefined,
  eingangDatumBis: undefined,
  antragstellerName: undefined,
  projektTitel: undefined,
  beantragtesBudgetVon: undefined,
  beantragtesBudgetBis: undefined,
  art: undefined,
  aktualisierungArt: [],
  aktualisierungDatumVon: undefined,
  aktualisierungDatumBis: undefined,
});

// Interface for Antrag list filter options
export interface AntragListFilterOptions {
  antragstellerNamen: string[];
  projektTitel: string[];
}
