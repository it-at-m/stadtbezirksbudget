import type { AntragDetails } from "@/types/AntragDetails.ts";

import {
  defaultCatchHandler,
  defaultResponseHandler,
  getConfig,
} from "@/api/fetch-utils.ts";
import { BACKEND } from "@/constants.ts";

/**
 * Fetches the details of an Antrag by its id.
 * @param id - The id of the Antrag to get
 * @returns A promise that to the details of the requested Antrag
 */
export function getAntragDetails(id: string): Promise<AntragDetails> {
  return fetch(`${BACKEND}/antrag/${id}`, getConfig())
    .then((response) => {
      defaultResponseHandler(response);
      return response.json();
    })
    .catch((err) => {
      return defaultCatchHandler(err);
    });
}
