import { readonly, ref } from "vue";

import { updateAntragStatus } from "@/api/update-antragStatus.ts";
import { STATUS_INDICATORS } from "@/constants.ts";
import { useSnackbarStore } from "@/stores/snackbar.ts";
import { Status, StatusText } from "@/types/Status.ts";

/**
 * Composable function that manages the status selection for an Antrag.
 * It provides the current status, available status options, and a method
 * to update the status both locally and in the backend.
 *
 * @param antragId - The ID of the Antrag whose status is being managed.
 * @param initialValue - The initial status value of the Antrag.
 * @returns {Object} An object exposing the current status, available
 * status options, and a method to update the status.
 */
export function useAntragStatusSelect(antragId: string, initialValue: Status) {
  const snackbarStore = useSnackbarStore();

  const status = ref<Status | undefined>(initialValue);
  const oldStatus = ref<Status>(initialValue);
  const search = ref<string>("");
  const statusOptions = Object.values(Status).map((status) => ({
    value: status,
    ...StatusText[status],
  }));

  /**
   * Updates the status of the Antrag both locally and in the backend.
   * @param newStatus - The new status to set for the Antrag.
   */
  function updateStatus(newStatus: Status) {
    if (!newStatus) return;
    updateAntragStatus(antragId, newStatus)
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
   * Resets the status to the old value if focus is lost without a change.
   * @param focus
   */
  function resetStatus(focus: boolean) {
    if (focus) {
      search.value = StatusText[oldStatus.value].shortText;
      status.value = undefined;
    } else {
      status.value = oldStatus.value;
      search.value = "";
    }
  }

  return {
    updateStatus,
    resetStatus,
    // Current status of the Antrag
    status,
    // Search term for filtering status options
    search,
    // Available status options
    statusOptions: readonly(statusOptions),
  };
}
