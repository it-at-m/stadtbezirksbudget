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

/**
 * Fetches a paginated list of Antrag summaries from the backend, applying the given filters.
 * @param page - The page number to fetch.
 * @param size - The number of items per page.
 * @param filters - The filters to apply to the Antrag list.
 * @returns A promise that resolves to a paginated list of Antrag summaries.
 */
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
