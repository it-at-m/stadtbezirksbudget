import type {
  AntragListFilter,
  AntragListFilterDTO,
} from "@/types/AntragListFilter.ts";

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

export function antragListFilterToDTO(
  filters: AntragListFilter
): AntragListFilterDTO {
  const { eingangDatum, art, aktualisierungDatum, ...rest } = filters;
  return {
    ...rest,
    eingangDatumVon: eingangDatum.at(0)?.toISOString(),
    eingangDatumBis: eingangDatum.at(-1)?.toISOString(),
    istFehlbetrag: art ? art === "Fehl" : undefined,
    aktualisierungDatumVon: aktualisierungDatum.at(0)?.toISOString(),
    aktualisierungDatumBis: aktualisierungDatum.at(-1)?.toISOString(),
  };
}
