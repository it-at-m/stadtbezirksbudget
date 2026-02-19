import type { AntragReference } from "@/types/AntragReference.ts";

import {
  defaultCatchHandler,
  defaultResponseHandler,
  patchConfig,
} from "@/api/fetch-utils.ts";
import { BACKEND } from "@/constants.ts";

/**
 * Updates the references of an Antrag.
 * @param antragId - The ID of the Antrag to update.
 * @param references - The new references to set for the Antrag.
 * @returns A promise that resolves when the references are updated.
 */
export function updateAntragReference(
  antragId: string,
  references: AntragReference
): Promise<void> {
  return fetch(
    `${BACKEND}/antrag/${antragId}/reference`,
    patchConfig(references)
  )
    .then(defaultResponseHandler)
    .catch((err) => {
      return defaultCatchHandler(err);
    });
}
