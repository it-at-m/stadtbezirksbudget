import type { AktualisierungArt } from "@/types/AktualisierungArt.ts";
import type { Status } from "@/types/Status.ts";

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
