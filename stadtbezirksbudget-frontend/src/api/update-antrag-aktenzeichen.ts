import type { Status } from "@/types/Status.ts";

import {
  defaultCatchHandler,
  defaultResponseHandler,
  patchConfig,
} from "@/api/fetch-utils.ts";
import { BACKEND } from "@/constants.ts";

export function updateAntragAktenzeichen(
  antragId: string,
  newAktenzeichen: string
): Promise<void> {
  return fetch(
    `${BACKEND}/antrag/${antragId}/aktenzeichen`,
    patchConfig({
      aktenzeichen: newAktenzeichen,
    })
  )
    .then(defaultResponseHandler)
    .catch((err) => {
      return defaultCatchHandler(err);
    });
}
