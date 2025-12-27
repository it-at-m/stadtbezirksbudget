import type { AktualisierungArt } from "@/types/AktualisierungArt.ts";
import type { FinanzierungArt } from "@/types/FinanzierungArt.ts";
import type { Status } from "@/types/Status.ts";

// Interface for filtering Antrag list
export interface AntragListFilter {
  search: string | null;
  status: Status[];
  bezirksausschussNr: number[];
  eingangDatumVon: Date | null;
  eingangDatumBis: Date | null;
  antragstellerName: string | null;
  projektTitel: string | null;
  beantragtesBudgetVon: number | null;
  beantragtesBudgetBis: number | null;
  finanzierungArt: FinanzierungArt | null;
  aktualisierungArt: AktualisierungArt[];
  aktualisierungDatumVon: Date | null;
  aktualisierungDatumBis: Date | null;
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
  search: null,
  status: [],
  bezirksausschussNr: [],
  eingangDatumVon: null,
  eingangDatumBis: null,
  antragstellerName: null,
  projektTitel: null,
  beantragtesBudgetVon: null,
  beantragtesBudgetBis: null,
  finanzierungArt: null,
  aktualisierungArt: [],
  aktualisierungDatumVon: null,
  aktualisierungDatumBis: null,
});

// Interface for Antrag list filter options
export interface AntragListFilterOptions {
  antragstellerNamen: string[];
  projektTitel: string[];
}
