import type AntragSummary from "@/types/AntragSummary.ts";
import type Page from "@/types/Page.ts";

import { computed, readonly, ref } from "vue";

import { getAntragList } from "@/api/fetch-antragList.ts";
import { useAntragListSort } from "@/composables/useAntragListSort.ts";
import { ROUTES_DETAILS, STATUS_INDICATORS } from "@/constants.ts";
import router from "@/plugins/router.ts";
import { useAntragListFilterStore } from "@/stores/useAntragListFilterStore.ts";
import { useAntragListSortingStore } from "@/stores/useAntragListSortingStore.ts";
import { useSnackbarStore } from "@/stores/useSnackbarStore.ts";
import { antragListSortToSortItem } from "@/types/AntragListSort.ts";

/**
 * Composable function that manages the state and operations for a list of
 * AntragSummary items. It handles fetching data, pagination, and loading
 * states, as well as displaying messages through a snackbar.
 *
 * @returns {Object} An object exposing the state and methods for managing
 * AntragSummary items.
 */
export function useAntragList() {
  const snackbarStore = useSnackbarStore();
  const filterStore = useAntragListFilterStore();
  const sortingStore = useAntragListSortingStore();
  const antragListSort = useAntragListSort();

  const items = ref<AntragSummary[]>([]);
  const totalItems = ref<number>(0);
  const page = ref<number>(1);
  const itemsPerPage = ref<number>(10);
  const loading = ref<boolean>(false);

  // Computed property for the current sorting option
  const sortBy = computed({
    get: () => antragListSortToSortItem(sortingStore.sorting),
    set: (value) => antragListSort.updateSortingWithSortItem(value),
  });

  /**
   * Fetches the AntragSummary items from the API based on the current
   * page and items per page. Updates the state with the fetched data
   * and handles any errors by displaying a message in the snackbar.
   */
  function fetchItems() {
    loading.value = true;
    getAntragList(
      page.value - 1,
      itemsPerPage.value,
      filterStore.filters,
      sortingStore.sorting
    )
      .then((content: Page<AntragSummary>) => {
        items.value = content.content;
        totalItems.value = content.page.totalElements;
      })
      .catch((error) => {
        snackbarStore.showMessage({
          message: error?.message || "Fehler beim Laden der Anträge",
          level: STATUS_INDICATORS.WARNING,
        });
      })
      .finally(() => {
        loading.value = false;
      });
  }

  /**
   * Updates the pagination options and triggers a fetch for new items.
   *
   * @param {Object} options - The new options for pagination.
   * @param {number} options.page - The new page number.
   * @param {number} options.itemsPerPage - The new number of items per page.
   */
  function updateOptions({
    page: newPage,
    itemsPerPage: newItemsPerPage,
  }: {
    page: number;
    itemsPerPage: number;
  }) {
    page.value = newPage;
    itemsPerPage.value = newItemsPerPage;
    fetchItems();
  }

  /**
   * Navigates to the details view of a specific AntragSummary item.
   * @param {MouseEvent} _
   * @param {Object} props
   * @param {AntragSummary} props.item - The AntragSummary item to view.
   */
  function goToDetails(_: MouseEvent, props: { item: AntragSummary }) {
    router
      .push({ name: ROUTES_DETAILS, params: { id: props.item.id } })
      .catch(() => {
        snackbarStore.showMessage({
          message: "Fehler beim Öffnen der Detailansicht",
          level: STATUS_INDICATORS.WARNING,
        });
      });
  }

  filterStore.$subscribe(() => {
    page.value = 1;
    fetchItems();
  });

  return {
    // List of AntragSummary items.
    items: readonly(items),
    // Total number of AntragSummary items available.
    totalItems: readonly(totalItems),
    // Current page number.
    page: readonly(page),
    // Number of items displayed per page.
    itemsPerPage: readonly(itemsPerPage),
    // Indicating whether data is currently being loaded.
    loading: readonly(loading),
    sortBy,
    fetchItems,
    updateOptions,
    goToDetails,
  };
}
