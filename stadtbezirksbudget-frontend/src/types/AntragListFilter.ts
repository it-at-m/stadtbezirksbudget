import type { AktualisierungArt } from "@/types/AktualisierungArt.ts";
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
  aktualisierungArt: AktualisierungArt[];
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

export const emptyAntragListFilter = () => ({
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
