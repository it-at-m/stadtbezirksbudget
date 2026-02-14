import type FrontendConfig from "@/types/FrontendConfig";

import {
  defaultCatchHandler,
  defaultResponseHandler,
  getConfig,
} from "@/api/fetch-utils.ts";
import { BACKEND } from "@/constants";

/**
 * Fetches the frontend configuration from the backend.
 * @returns A promise that resolves to the FrontendConfig.
 */
export function getFrontendConfig(): Promise<FrontendConfig> {
  return fetch(`${BACKEND}/frontend-config`, getConfig())
    .then((response) => {
      defaultResponseHandler(response);
      return response.json();
    })
    .catch((error) => defaultCatchHandler(error));
}
