import type {
  AntragListSort,
  AntragListSortOption,
} from "@/types/AntragListSort.ts";

import { ref } from "vue";

import { useAntragListSortingStore } from "@/stores/useAntragListSortingStore.ts";
import { createEmptyListSort } from "@/types/AntragListSort.ts";

/**
 * Composable for managing the Antrag list sorting options.
 * Provides reactive sorting and methods to update and reset them.
 */
export function useAntragListSort() {
  const sortingStore = useAntragListSortingStore();

  // The current applied sorting option
  const sortOption = ref<AntragListSort>({ ...sortingStore.sorting });

  /**
   * Updates the store with the current sorting options.
   * @param newValue new sorting option
   */
  function updateSorting(newValue: AntragListSortOption) {
    resetSorting();

    sortingStore.setListSorting({
      ...createEmptyListSort(),
      [newValue.sortBy]: newValue,
    });
    sortOption.value[newValue.sortBy] = newValue;
  }

  /**
   * Resets the current sorting options.
   */
  function resetSorting() {
    sortingStore.setListSorting(createEmptyListSort());
    sortOption.value = createEmptyListSort();
  }

  return { sortOption, resetSorting, updateSorting };
}
