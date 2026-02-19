import type { AntragReference } from "@/types/AntragReference.ts";

import {
  defaultCatchHandler,
  defaultResponseHandler,
  patchConfig,
} from "@/api/fetch-utils.ts";
import { BACKEND } from "@/constants.ts";

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
