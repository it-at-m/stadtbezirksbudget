import type { Status } from "@/types/Status.ts";
import type { Ref } from "vue";

import { readonly, ref, watch } from "vue";

import { updateAntragStatus } from "@/api/update-antragStatus.ts";
import { STATUS_INDICATORS } from "@/constants.ts";
import { useSnackbarStore } from "@/stores/snackbar.ts";
import { statusOptions, StatusText } from "@/types/Status.ts";

/**
 * Composable function that manages the status selection for an Antrag.
 * It provides the current status, available status options, and a method
 * to update the status both locally and in the backend.
 *
 * @param antragId - The ID of the Antrag whose status is being managed.
 * @param initialStatus - The initial status value of the Antrag.
 * @returns {Object} An object exposing the current status, available
 * status options, and a method to update the status.
 */
export function useAntragStatusSelect(
  antragId: Ref<string>,
  initialStatus: Ref<Status>
) {
  const snackbarStore = useSnackbarStore();

  const status = ref<Status | undefined>(initialStatus.value);
  const oldStatus = ref<Status>(initialStatus.value);
  const search = ref<string>("");

  /**
   * Updates the status of the Antrag both locally and in the backend.
   * @param newStatus - The new status to set for the Antrag.
   */
  function updateStatus(newStatus: Status) {
    if (!newStatus) return;
    updateAntragStatus(antragId.value, newStatus)
      .then(() => {
        status.value = newStatus;
        oldStatus.value = newStatus;
        snackbarStore.showMessage({
          message: `Antragsstatus aktualisiert`,
          level: STATUS_INDICATORS.SUCCESS,
        });
      })
      .catch((error) => {
        status.value = oldStatus.value;
        snackbarStore.showMessage({
          message:
            error?.message || "Fehler beim Aktualisieren des Antragsstatus",
          level: STATUS_INDICATORS.WARNING,
        });
      })
      .finally(() => {
        (document.activeElement as HTMLElement)?.blur();
      });
  }

  /**
   * Toggles status and search based on focus state.
   *
   * @param {boolean} focus - If focused updates search and clears status,
   *                          If not focused restores status and clears search.
   */
  function toggleStatusAndSearch(focus: boolean) {
    if (focus) {
      search.value = StatusText[oldStatus.value].shortText;
      status.value = undefined;
    } else {
      status.value = oldStatus.value;
      search.value = "";
    }
  }

  watch(
    () => initialStatus.value,
    (initial) => {
      status.value = initial;
      oldStatus.value = initial;
    }
  );

  return {
    updateStatus,
    toggleStatusAndSearch,
    // Current status of the Antrag
    status,
    // Search term for filtering status options
    search,
    // Available status options
    statusOptions: readonly(statusOptions),
  };
}
