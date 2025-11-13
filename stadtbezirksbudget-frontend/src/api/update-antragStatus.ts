import type { Status } from "@/types/Status.ts";

import {
  defaultCatchHandler,
  defaultResponseHandler,
  patchConfig,
} from "@/api/fetch-utils.ts";
import { BACKEND } from "@/constants.ts";

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
      return defaultCatchHandler(
        err,
        "Fehler beim Aktualisieren des Antragsstatus"
      );
    });
}
