import type { AntragListFilter } from "@/types/AntragListFilter.ts";

import { ref } from "vue";

import { useAntragListFilterStore } from "@/stores/antragListFilter.ts";

export function useAntragListFilter() {
  const filterStore = useAntragListFilterStore();

  const filters = ref<AntragListFilter>({});

  function updateFilters() {
    if (
      JSON.stringify(filterStore.getFilters) === JSON.stringify(filters.value)
    )
      return;
    filterStore.setFilters({ ...filters.value });
  }

  function resetFilters() {
    filters.value = {};
    updateFilters();
  }

  return {
    updateFilters,
    resetFilters,
    filters,
  };
}
