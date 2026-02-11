import type FrontendConfig from "@/types/FrontendConfig";

import { defineStore } from "pinia";
import { computed, ref } from "vue";

export const useConfigStore = defineStore("config", () => {
  const config = ref<FrontendConfig | null>(null);

  const getConfig = computed((): FrontendConfig | null => config.value);

  function setConfig(payload: FrontendConfig): void {
    config.value = payload;
  }

  const getZammadBaseUrl = computed(
    (): string | undefined => config.value?.zammadBaseUrl
  );
  const getEakteBaseUrl = computed(
    (): string | undefined => config.value?.eakteBaseUrl
  );

  return { getConfig, setConfig, getZammadBaseUrl, getEakteBaseUrl };
});
