import type { AntragListFilter } from "@/types/AntragListFilter.ts";

import { toLocalISOString } from "@/util/formatter.ts";

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

/**
 * Converts AntragListFilter to AntragListFilterDTO
 * @param filters - The AntragListFilter object to convert
 * @returns The converted AntragListFilterDTO object
 */
export function antragListFilterToDTO(
  filters: AntragListFilter
): AntragListFilterDTO {
  const {
    eingangDatumVon,
    eingangDatumBis,
    art,
    aktualisierungDatumVon,
    aktualisierungDatumBis,
    ...rest
  } = filters;
  eingangDatumBis?.setHours(23, 59, 59, 999);
  aktualisierungDatumBis?.setHours(23, 59, 59, 999);
  return {
    ...rest,
    eingangDatumVon: toLocalISOString(eingangDatumVon),
    eingangDatumBis: toLocalISOString(eingangDatumBis),
    istFehlbetrag: art ? art === "Fehl" : undefined,
    aktualisierungDatumVon: toLocalISOString(aktualisierungDatumVon),
    aktualisierungDatumBis: toLocalISOString(aktualisierungDatumBis),
  };
}
