import type { AntragListFilter } from "@/types/AntragListFilter.ts";

import { defineStore } from "pinia";
import { computed, ref } from "vue";

import { emptyAntragListFilter } from "@/types/AntragListFilter.ts";

export const useAntragListFilterStore = defineStore("antragListFilter", () => {
  const filters = ref<AntragListFilter>({ ...emptyAntragListFilter });

  const getFilters = computed((): AntragListFilter => {
    return filters.value;
  });

  function setFilters(payload: AntragListFilter): void {
    filters.value = payload;
  }

  return { filters, getFilters, setFilters };
});
