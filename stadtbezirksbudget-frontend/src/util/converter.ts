import type {
  AntragListFilter,
  AntragListFilterDTO,
} from "@/types/AntragListFilter.ts";

export function appendSearchParams(
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

export function antragListFilterToDTO(
  filters: AntragListFilter
): AntragListFilterDTO {
  return {
    ...filters,
    eingangDatumVon: filters.eingangDatum?.at(0),
    eingangDatumBis: filters.eingangDatum?.at(-1),
    istFehlbetrag: filters.art ? filters.art === "Fehl" : undefined,
    aktualisierungDatumVon: filters.aktualisierungDatum?.at(0),
    aktualisierungDatumBis: filters.aktualisierungDatum?.at(-1),
  };
}
