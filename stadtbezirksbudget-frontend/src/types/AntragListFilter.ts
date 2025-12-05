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

// Data Transfer Object for AntragListFilter
export interface AntragListFilterDTO extends Omit<
  AntragListFilter,
  | "eingangDatumVon"
  | "eingangDatumBis"
  | "art"
  | "aktualisierungDatumVon"
  | "aktualisierungDatumBis"
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
