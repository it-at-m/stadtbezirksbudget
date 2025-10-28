import type AntragSummary from "@/types/AntragSummary.ts";
import type Page from "@/types/Page.ts";

import { defaultResponseHandler, getConfig } from "@/api/fetch-utils.ts";
import { BACKEND } from "@/constants.ts";

export function getAntragsSummaryList(
  page: number,
  size: number
): Promise<Page<AntragSummary>> {
  return fetch(
    `${BACKEND}/antrag?page=${page}&size=${size}`,
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
