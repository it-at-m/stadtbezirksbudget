import { ref } from "vue";

import { useAntragListFilterStore } from "@/stores/useAntragListFilterStore.ts";

/**
 * Composable for searching the antrag list
 * Provides reactive query and method to search.
 */
export function useAntragListSearch() {
  const filterStore = useAntragListFilterStore();

  // Current search query, initialized from the filters store
  const query = ref<string | null>(filterStore.filters.search);

  /**
   * Searches the antrag list by the current query
   */
  function search() {
    filterStore.setSearch(query.value);
  }

  return {
    query,
    search,
  };
}
