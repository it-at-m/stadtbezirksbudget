import type { Status } from "@/types/Status.ts";

export interface AntragListFilter {
  status: Status[];
  bezirksausschussNr: number[];
  eingangDatum: string[];
  antragstellerName?: string;
  projektTitel?: string;
  beantragtesBudgetVon?: number;
  beantragtesBudgetBis?: number;
  art?: string;
  aktualisierungArt: string[];
  aktualisierungDatum: string[];
}

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

const emptyAntragListFilter: AntragListFilter = {
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
} as const;

export function getEmptyAntragListFilter(): AntragListFilter {
  return JSON.parse(JSON.stringify(emptyAntragListFilter));
}
