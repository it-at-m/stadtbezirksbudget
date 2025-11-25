import type { Status } from "@/types/Status.ts";

export interface AntragListFilter {
  status?: Status[];
  bezirksausschussNr?: number[];
  eingangDatum?: string[];
  antragstellerName?: string;
  projektTitel?: string;
  beantragtesBudgetVon?: number;
  beantragtesBudgetBis?: number;
  art?: string;
  aktualisierungArt?: string[];
  aktualisierungDatum?: string[];
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
