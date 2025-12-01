import type { AktualisierungArt } from "@/types/AktualisierungArt.ts";
import type { Status } from "@/types/Status.ts";

// Interface for filtering Antrag list
export interface AntragListFilter {
  status: Status[];
  bezirksausschussNr: number[];
  eingangDatum: Date[];
  antragstellerName?: string;
  projektTitel?: string;
  beantragtesBudgetVon?: number;
  beantragtesBudgetBis?: number;
  art?: string;
  aktualisierungArt: AktualisierungArt[];
  aktualisierungDatum: Date[];
}

// Data Transfer Object for AntragListFilter
export interface AntragListFilterDTO
  extends Omit<
    AntragListFilter,
    "eingangDatum" | "art" | "aktualisierungDatum"
  > {
  eingangDatumVon?: string;
  eingangDatumBis?: string;
  istFehlbetrag?: boolean;
  aktualisierungDatumVon?: string;
  aktualisierungDatumBis?: string;
}

// Creates an empty AntragListFilter object
export const emptyAntragListFilter = (): AntragListFilter => ({
  status: [],
  bezirksausschussNr: [],
  eingangDatum: [],
  antragstellerName: undefined,
  projektTitel: undefined,
  beantragtesBudgetVon: undefined,
  beantragtesBudgetBis: undefined,
  art: undefined,
  aktualisierungArt: [],
  aktualisierungDatum: [],
});

// Interface for Antrag list filter options
export interface AntragListFilterOptions {
  antragstellerNamen: string[];
  projektTitel: string[];
}
