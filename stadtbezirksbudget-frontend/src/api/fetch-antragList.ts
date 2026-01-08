import type { AntragListFilter } from "@/types/AntragListFilter.ts";
import type { AntragListSort } from "@/types/AntragListSort.ts";
import type AntragSummary from "@/types/AntragSummary.ts";
import type Page from "@/types/Page.ts";

import {
  defaultCatchHandler,
  defaultResponseHandler,
  getConfig,
} from "@/api/fetch-utils.ts";
import { BACKEND } from "@/constants.ts";
import { antragListFilterToDTO } from "@/types/AntragListFilterDTO.ts";
import { antragListSortToSortDto } from "@/types/AntragListSort.ts";
import { objectToSearchParams } from "@/util/converter.ts";

/**
 * Fetches a paginated list of Antrag summaries from the backend, applying the given filters.
 * @param page - The page number to fetch.
 * @param size - The number of items per page.
 * @param filters - The filters to apply to the Antrag list.
 * @param sorting - The sorting to apply to the Antrag list.
 * @returns A promise that resolves to a paginated list of Antrag summaries.
 */
export function getAntragList(
  page: number,
  size: number,
  filters: AntragListFilter,
  sorting: AntragListSort
): Promise<Page<AntragSummary>> {
  const filtersDto = antragListFilterToDTO(filters);
  const sortingDto = antragListSortToSortDto(sorting);
  const params = new URLSearchParams({
    page: String(page),
    size: String(size),
  });
  objectToSearchParams(filtersDto, params);
  objectToSearchParams(sortingDto, params);

  return fetch(`${BACKEND}/antrag?${params}`, getConfig())
    .then((response) => {
      defaultResponseHandler(response);
      return response.json();
    })
    .catch((err) => {
      return defaultCatchHandler(err, "Fehler beim Laden der Antragsliste.");
    });
}
