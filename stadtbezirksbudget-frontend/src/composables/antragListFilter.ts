import type { AntragListFilter } from "@/types/AntragListFilter.ts";

import { ref } from "vue";

import { useAntragListFilterStore } from "@/stores/antragListFilter.ts";
import { emptyAntragListFilter } from "@/types/AntragListFilter.ts";

/**
 * Composable for managing the Antrag list filters.
 * Provides reactive filters and methods to update and reset them.
 */
export function useAntragListFilter() {
  const filterStore = useAntragListFilterStore();

  // Current filters, initialized from the store
  const filters = ref<AntragListFilter>({ ...filterStore.filters });

  /**
   * Updates the store with the current filters if they have changed.
   */
  function updateFilters() {
    if (JSON.stringify(filterStore.filters) !== JSON.stringify(filters.value)) {
      filterStore.setFilters({ ...filters.value });
    }
  }

  /**
   * Resets the filters to their default empty state and updates the store.
   */
  function resetFilters() {
    filters.value = emptyAntragListFilter();
    updateFilters();
  }

  return {
    updateFilters,
    resetFilters,
    filters,
  };
}
