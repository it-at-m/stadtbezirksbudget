import type { AntragListFilter } from "@/types/AntragListFilter.ts";

import { defineStore } from "pinia";
import { ref } from "vue";

import { defaultAntragListFilter } from "@/types/AntragListFilter.ts";

/**
 * Pinia store for managing the filters applied to the Antrag list.
 */
export const useAntragListFilterStore = defineStore("antragListFilter", () => {
  // State: The current filters applied to the Antrag list.
  const filters = ref<AntragListFilter>(defaultAntragListFilter());

  /**
   * Sets the filters for the Antrag list.
   * @param payload - The new filters to apply.
   */
  function setFilters(payload: AntragListFilter): void {
    filters.value = payload;
  }

  return { filters, setFilters };
});
