import type Dummy from "@/types/Dummy.ts";
import type Page from "@/types/Page.ts";

import { defaultResponseHandler, getConfig } from "@/api/fetch-utils.ts";
import { BACKEND } from "@/constants.ts";

export function getDummyList(page: number, size: number): Promise<Page<Dummy>> {
  return fetch(
    `${BACKEND}/antragsdatenSubset?page=${page}&size=${size}`,
    getConfig()
  )
    .then((response) => {
      defaultResponseHandler(response);
      return response.json();
    })
    .catch((err) => {
      defaultResponseHandler(err);
    });
}
