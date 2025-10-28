import type AntragSummary from "@/types/AntragSummary.ts";
import type Page from "@/types/Page.ts";

import { readonly, ref } from "vue";

import { getAntragsSummaryList } from "@/api/fetch-antragSummary-list.ts";
import { STATUS_INDICATORS } from "@/constants.ts";
import { useSnackbarStore } from "@/stores/snackbar.ts";

export function useAntragSummaryList() {
  const snackbarStore = useSnackbarStore();

  const items = ref<AntragSummary[]>([]);
  const totalItems = ref(0);
  const page = ref<number>(1);
  const itemsPerPage = ref<number>(10);
  const loading = ref(false);

  function fetchItems() {
    loading.value = true;
    getAntragsSummaryList(page.value - 1, itemsPerPage.value)
      .then((content: Page<AntragSummary>) => {
        items.value = content.content;
        totalItems.value = content.page.totalElements;
      })
      .catch((error) => {
        snackbarStore.showMessage({
          message: error?.messsage || "Fehler beim Laden der Anträge",
          level: STATUS_INDICATORS.WARNING,
        });
      })
      .finally(() => {
        loading.value = false;
      });
  }

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

  return {
    items: readonly(items),
    totalItems: readonly(totalItems),
    page: readonly(page),
    itemsPerPage: readonly(itemsPerPage),
    loading: readonly(loading),
    fetchItems,
    updateOptions,
  };
}
