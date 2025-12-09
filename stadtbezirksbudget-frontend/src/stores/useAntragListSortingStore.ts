import type { AntragListSort } from "@/types/AntragListSort.ts";

import { defineStore } from "pinia";
import { ref } from "vue";

import { createEmptyListSort } from "@/types/AntragListSort.ts";

/**
 * Pinia store for managing the sorting options applied to the Antrag list.
 */
export const useAntragListSortingStore = defineStore("antragListSort", () => {
  // State: The current sorting options applied to the Antrag list.
  const sorting = ref<AntragListSort>(createEmptyListSort());

  /**
   * Sets the sorting options for the Antrag list.
   * @param payload - The new sorting options to apply.
   */
  function setSorting(payload: AntragListSort): void {
    sorting.value = payload;
  }

  return { sorting, setSorting };
});
