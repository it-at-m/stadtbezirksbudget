import type { MaybeRefOrGetter } from "vue";

import { computed, toValue } from "vue";

import { EAKTE_PATH, ZAMMAD_TICKET_PATH } from "@/constants.ts";
import { useConfigStore } from "@/stores/useConfigStore.ts";

export function useReferenceLink() {
  const configStore = useConfigStore();

  function getZammadLink(zammadNr: MaybeRefOrGetter<string>) {
    return computed(() => {
      const baseUrl = configStore.getZammadBaseUrl;
      const nr = toValue(zammadNr);
      return baseUrl ? `${baseUrl}${ZAMMAD_TICKET_PATH}${nr}` : undefined;
    });
  }

  function getEakteLink(eakteCooAdresse: MaybeRefOrGetter<string>) {
    return computed(() => {
      const baseUrl = configStore.getEakteBaseUrl;
      const adresse = toValue(eakteCooAdresse);
      return baseUrl ? `${baseUrl}${EAKTE_PATH}${adresse}` : undefined;
    });
  }

  return { getZammadLink, getEakteLink };
}
