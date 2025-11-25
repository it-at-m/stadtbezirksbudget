import type { AntragListFilter } from "@/types/AntragListFilter.ts";

import { ref } from "vue";

import { useAntragListFilterStore } from "@/stores/antragListFilter.ts";
import { emptyAntragListFilter } from "@/types/AntragListFilter.ts";

export function useAntragListFilter() {
  const filterStore = useAntragListFilterStore();

  const filters = ref<AntragListFilter>(emptyAntragListFilter());

  function updateFilters() {
    if (JSON.stringify(filterStore.filters) !== JSON.stringify(filters.value)) {
      filterStore.setFilters({ ...filters.value });
    }
  }

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
