import type { AntragListFilterOptions } from "@/types/AntragListFilter.ts";

import { defineStore } from "pinia";
import { ref } from "vue";

/**
 * Pinia store to manage the filter options for the Antrag list.
 */
export const useAntragListFilterOptionsStore = defineStore(
  "antragListFilterOptions",
  () => {
    // State: Filter options for the Antrag list
    const filterOptions = ref<AntragListFilterOptions>({
      antragstellerNamen: [],
      projektTitel: [],
    });

    /**
     * Sets the filter options for the Antrag list.
     * @param payload - The new filter options to set.
     */
    function setFilterOptions(payload: AntragListFilterOptions): void {
      filterOptions.value = payload;
    }

    return { filterOptions, setFilterOptions };
  }
);
