import type { AntragListFilterOptions } from "@/types/AntragListFilter.ts";

import { onMounted } from "vue";

import { getAntragListFilterOptions } from "@/api/fetch-antragListFilterOptions.ts";
import { STATUS_INDICATORS } from "@/constants.ts";
import { useAntragListFilterOptionsStore } from "@/stores/antragListFilterOptions.ts";
import { useSnackbarStore } from "@/stores/snackbar.ts";

/**
 * Composable to initialize stores by fetching necessary data on component mount.
 */
export function useInitializeStores() {
  const snackbarStore = useSnackbarStore();
  const antragListFilterOptionsStore = useAntragListFilterOptionsStore();

  function fetchFilterOptions() {
    getAntragListFilterOptions()
      .then((options: AntragListFilterOptions) => {
        antragListFilterOptionsStore.setFilterOptions(options);
      })
      .catch((error) => {
        snackbarStore.showMessage({
          message: error?.message || "Fehler beim Laden der Filteroptionen",
          level: STATUS_INDICATORS.WARNING,
        });
      });
  }

  onMounted(() => {
    fetchFilterOptions();
  });
}
