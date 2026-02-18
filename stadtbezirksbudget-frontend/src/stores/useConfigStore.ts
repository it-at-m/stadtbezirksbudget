import type FrontendConfig from "@/types/FrontendConfig";

import { defineStore } from "pinia";
import { computed, ref } from "vue";

/**
 * Pinia store to manage the frontend configuration, which includes base URLs for external services like Zammad and Eakte.
 */
export const useConfigStore = defineStore("config", () => {
  // State: The frontend configuration, which can be null if it has not been loaded yet.
  const config = ref<FrontendConfig | null>(null);

  /**
   * Sets the frontend configuration in the store.
   * @param payload - The new frontend configuration to store.
   */
  function setConfig(payload: FrontendConfig): void {
    config.value = payload;
  }

  /**
   * Computed property to get the base URL for Zammad from the configuration.
   * @returns The Zammad base URL or undefined if not set.
   */
  const getZammadBaseUrl = computed(
    (): string | undefined => config.value?.zammadBaseUrl
  );

  /**
   * Computed property to get the base URL for Eakte from the configuration.
   * @returns The Eakte base URL or undefined if not set.
   */
  const getEakteBaseUrl = computed(
    (): string | undefined => config.value?.eakteBaseUrl
  );

  return { config, setConfig, getZammadBaseUrl, getEakteBaseUrl };
});
