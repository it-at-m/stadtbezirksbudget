import type { Status } from "@/types/Status.ts";

import {
  defaultCatchHandler,
  defaultResponseHandler,
  patchConfig,
} from "@/api/fetch-utils.ts";
import { BACKEND } from "@/constants.ts";

/**
 * Updates the status of an Antrag.
 * @param antragId - The ID of the Antrag to update.
 * @param newStatus - The new status to set for the Antrag.
 * @returns A promise that resolves when the status is updated.
 */
export function updateAntragStatus(
  antragId: string,
  newStatus: Status
): Promise<void> {
  return fetch(
    `${BACKEND}/antrag/${antragId}/status`,
    patchConfig({
      status: newStatus,
    })
  )
    .then(defaultResponseHandler)
    .catch((err) => {
      return defaultCatchHandler(err);
    });
}
