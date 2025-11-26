import type { AntragListFilter } from "@/types/AntragListFilter.ts";
import type AntragSummary from "@/types/AntragSummary.ts";
import type Page from "@/types/Page.ts";

import {
  defaultCatchHandler,
  defaultResponseHandler,
  getConfig,
} from "@/api/fetch-utils.ts";
import { BACKEND } from "@/constants.ts";
import {
  antragListFilterToDTO,
  objectToSearchParams,
} from "@/util/converter.ts";

export function getAntragsSummaryList(
  page: number,
  size: number,
  filters: AntragListFilter
): Promise<Page<AntragSummary>> {
  const filtersDto = antragListFilterToDTO(filters);
  const params = new URLSearchParams({
    page: String(page),
    size: String(size),
  });
  objectToSearchParams(filtersDto, params);
  return fetch(`${BACKEND}/antrag?${params}`, getConfig())
    .then((response) => {
      defaultResponseHandler(response);
      return response.json();
    })
    .catch((err) => {
      return defaultCatchHandler(err, "Fehler beim Laden der Antragsliste.");
    });
}
