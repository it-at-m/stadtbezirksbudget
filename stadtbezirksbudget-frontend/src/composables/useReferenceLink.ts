import type { MaybeRefOrGetter } from "vue";

import { computed, toValue } from "vue";

import { EAKTE_PATH, ZAMMAD_TICKET_PATH } from "@/constants.ts";
import { useConfigStore } from "@/stores/useConfigStore.ts";

/**
 * Composable to generate reference links for Zammad tickets and Eakte documents based on the frontend configuration.
 * It provides functions to create computed properties for the respective links, which will update automatically when the configuration changes.
 */
export function useReferenceLink() {
  const configStore = useConfigStore();

  /**
   * Generates a computed property for the Zammad ticket link based on the provided ticket number and the base URL from the configuration.
   * @param zammadNr - The ticket number, which can be a ref or a getter.
   * @returns A computed property that resolves to the full URL of the Zammad ticket or undefined if the base URL is not set.
   */
  function getZammadLink(zammadNr: MaybeRefOrGetter<string>) {
    return computed(() => {
      const baseUrl = configStore.getZammadBaseUrl;
      const nr = toValue(zammadNr);
      return baseUrl ? `${baseUrl}${ZAMMAD_TICKET_PATH}${nr}` : undefined;
    });
  }

  /**
   * Generates a computed property for the Eakte document link based on the provided COO address and the base URL from the configuration.
   * @param eakteCooAdresse - The COO address, which can be a ref or a getter.
   * @returns A computed property that resolves to the full URL of the Eakte document or undefined if the base URL is not set.
   */
  function getEakteLink(eakteCooAdresse: MaybeRefOrGetter<string>) {
    return computed(() => {
      const baseUrl = configStore.getEakteBaseUrl;
      const adresse = toValue(eakteCooAdresse);
      return baseUrl ? `${baseUrl}${EAKTE_PATH}${adresse}` : undefined;
    });
  }

  return { getZammadLink, getEakteLink };
}
