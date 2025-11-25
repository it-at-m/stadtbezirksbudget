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
  const { eingangDatum, art, aktualisierungDatum, ...rest } = filters;
  return {
    ...rest,
    eingangDatumVon: eingangDatum?.at(0),
    eingangDatumBis: eingangDatum?.at(-1),
    istFehlbetrag: art ? art === "Fehl" : undefined,
    aktualisierungDatumVon: aktualisierungDatum?.at(0),
    aktualisierungDatumBis: aktualisierungDatum?.at(-1),
  };
}
