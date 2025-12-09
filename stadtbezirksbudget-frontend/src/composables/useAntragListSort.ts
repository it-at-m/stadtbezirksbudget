import type {
  AntragListSort,
  AntragListSortOption,
} from "@/types/AntragListSort.ts";
import type { DataTableSortItem } from "vuetify/framework";

import { ref } from "vue";

import { useAntragListSortingStore } from "@/stores/useAntragListSortingStore.ts";
import {
  antragListSortOptionFromSortItems,
  createEmptyListSort,
} from "@/types/AntragListSort.ts";

/**
 * Composable for managing the Antrag list sorting options.
 * Provides reactive sorting and methods to update and reset them.
 */
export function useAntragListSort() {
  const sortingStore = useAntragListSortingStore();

  // The current applied sorting option
  const sorting = ref<AntragListSort>({ ...sortingStore.sorting });

  /**
   * Updates the store with the current sorting options.
   * @param newValue new sorting option
   */
  function updateSorting(newValue: AntragListSortOption) {
    sorting.value = {
      ...createEmptyListSort(),
      [newValue.sortBy]: newValue,
    };
    sortingStore.setSorting({ ...sorting.value });
  }

  /**
   * Updates the store with the current sorting options.
   * @param newValue array of SortItem (the first item is used)
   */
  function updateSortingWithSortItem(newValue: DataTableSortItem[]) {
    const newSorting = antragListSortOptionFromSortItems(newValue);
    if (newSorting === undefined) {
      resetSorting();
      return;
    }
    updateSorting(newSorting);
  }

  /**
   * Resets the current sorting options.
   */
  function resetSorting() {
    sortingStore.setSorting(createEmptyListSort());
    sorting.value = createEmptyListSort();
  }

  return {
    sorting,
    resetSorting,
    updateSorting,
    updateSortingWithSortItem,
  };
}
