import type {
  AntragListFilter,
  AntragListFilterDTO,
} from "@/types/AntragListFilter.ts";

import { toLocalISOString } from "@/util/formatter.ts";

/**
 * Converts an object to URLSearchParams.
 * - Ignores undefined, null, and empty string values.
 * - Joins array values with commas.
 * - Does not handle nested objects.
 * @param object - The object to convert
 * @param params - Optional existing URLSearchParams to append to
 * @returns The resulting URLSearchParams
 */
export function objectToSearchParams(
  object: object,
  params: URLSearchParams = new URLSearchParams()
): URLSearchParams {
  Object.entries(object).forEach(([key, value]) => {
    if (value === undefined || value === null || value === "") return;

    if (Array.isArray(value)) {
      if (value.length === 0) return;
      params.append(key, value.join(","));
    } else {
      params.append(key, String(value));
    }
  });
  return params;
}

/**
 * Converts AntragListFilter to AntragListFilterDTO
 * @param filters - The AntragListFilter object to convert
 * @returns The converted AntragListFilterDTO object
 */
export function antragListFilterToDTO(
  filters: AntragListFilter
): AntragListFilterDTO {
  const { eingangDatum, art, aktualisierungDatum, ...rest } = filters;
  return {
    ...rest,
    eingangDatumVon: toLocalISOString(eingangDatum.at(0)),
    eingangDatumBis: toLocalISOString(eingangDatum.at(-1)),
    istFehlbetrag: art ? art === "Fehl" : undefined,
    aktualisierungDatumVon: toLocalISOString(aktualisierungDatum.at(0)),
    aktualisierungDatumBis: toLocalISOString(aktualisierungDatum.at(-1)),
  };
}
