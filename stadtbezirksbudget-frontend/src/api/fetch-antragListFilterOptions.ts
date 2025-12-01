import type { AntragListFilterOptions } from "@/types/AntragListFilter.ts";

import {
  defaultCatchHandler,
  defaultResponseHandler,
  getConfig,
} from "@/api/fetch-utils.ts";
import { BACKEND } from "@/constants.ts";

/**
 * Fetches the filter options for the Antrag list from the backend.
 * @returns A promise that resolves to the AntragListFilterOptions.
 */
export function getAntragListFilterOptions(): Promise<AntragListFilterOptions> {
  return fetch(`${BACKEND}/antrag/filterOptions`, getConfig())
    .then((response) => {
      defaultResponseHandler(response);
      return response.json();
    })
    .catch((err) => {
      return defaultCatchHandler(err, "Fehler beim Laden der Filteroptionen.");
    });
}
