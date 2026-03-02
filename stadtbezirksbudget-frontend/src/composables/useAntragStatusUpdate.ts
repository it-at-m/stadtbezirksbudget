import type { Status } from "@/types/Status.ts";
import type { Ref } from "vue";

import { ref, watch } from "vue";

import { updateAntragStatus } from "@/api/update-antragStatus.ts";
import { STATUS_INDICATORS } from "@/constants.ts";
import { useSnackbarStore } from "@/stores/useSnackbarStore.ts";

/**
 * Composable function that manages the status selection for an Antrag.
 * It provides the current status, available status options, and a method
 * to update the status both locally and in the backend.
 *
 * @param antragId - The ID of the Antrag whose status is being managed.
 * @param initialStatus - The initial status value of the Antrag.
 * @param onUpdate - A callback function that is called after the status is successfully updated.
 * @returns {Object} An object exposing the current status, available
 * status options, and a method to update the status.
 */
export function useAntragStatusUpdate(
  antragId: Ref<string>,
  initialStatus: Ref<Status>,
  onUpdate: (newStatus: Status) => void
) {
  const snackbarStore = useSnackbarStore();

  const status = ref<Status | undefined>(initialStatus.value);
  const oldStatus = ref<Status>(initialStatus.value);
  const search = ref<string>("");

  /**
   * Updates the status of the Antrag both locally and in the backend.
   * @param newStatus - The new status to set for the Antrag.
   */
  function updateStatus(newStatus: Status | undefined) {
    if (!newStatus) return;
    updateAntragStatus(antragId.value, newStatus)
      .then(() => {
        status.value = newStatus;
        oldStatus.value = newStatus;
        snackbarStore.showMessage({
          message: "Antragsstatus aktualisiert",
          level: STATUS_INDICATORS.SUCCESS,
        });
        onUpdate(newStatus);
      })
      .catch(() => {
        status.value = oldStatus.value;
        snackbarStore.showMessage({
          message: "Fehler beim Aktualisieren des Antragsstatus",
          level: STATUS_INDICATORS.WARNING,
        });
      })
      .finally(() => {
        (document.activeElement as HTMLElement)?.blur();
      });
  }

  /**
   * Handles focus changes on input field.
   *
   * @param {boolean} focus - If not focused restores to old status.
   */
  function onFocusChange(focus: boolean) {
    if (!focus) {
      status.value = oldStatus.value;
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
    onFocusChange,
    // Current status of the Antrag
    status,
    // Search term for filtering status options
    search,
  };
}
