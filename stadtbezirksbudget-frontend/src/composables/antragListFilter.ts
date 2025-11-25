import type { AntragListFilter } from "@/types/AntragListFilter.ts";

import { ref } from "vue";

import { useAntragListFilterStore } from "@/stores/antragListFilter.ts";
import { getEmptyAntragListFilter } from "@/types/AntragListFilter.ts";

export function useAntragListFilter() {
  const filterStore = useAntragListFilterStore();

  const filters = ref<AntragListFilter>(getEmptyAntragListFilter());

  function updateFilters() {
    if (
      JSON.stringify(filterStore.getFilters) !== JSON.stringify(filters.value)
    ) {
      filterStore.setFilters({ ...filters.value });
    }
  }

  function resetFilters() {
    filters.value = getEmptyAntragListFilter();
    updateFilters();
  }

  return {
    updateFilters,
    resetFilters,
    filters,
  };
}
