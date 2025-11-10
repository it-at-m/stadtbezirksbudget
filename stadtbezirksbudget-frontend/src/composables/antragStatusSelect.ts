import { readonly, ref } from "vue";

import { updateAntragStatus } from "@/api/update-antragStatus.ts";
import { STATUS_INDICATORS } from "@/constants.ts";
import { useSnackbarStore } from "@/stores/snackbar.ts";
import { Status, StatusText } from "@/types/Status.ts";

export function useAntragStatusSelect(antragId: string, initialValue: Status) {
  const snackbarStore = useSnackbarStore();

  const status = ref<Status>(initialValue);
  const statusOptions = Object.values(Status).map((status) => ({
    title: StatusText[status],
    value: status,
  }));

  function updateStatus(newStatus: Status) {
    updateAntragStatus(antragId, newStatus)
      .then(() => {
        status.value = newStatus;
        snackbarStore.showMessage({
          message: `Antragsstatus aktualisiert`,
          level: STATUS_INDICATORS.SUCCESS,
        });
      })
      .catch((error) => {
        snackbarStore.showMessage({
          message:
            error?.message || "Fehler beim Aktualisieren des Antragsstatus",
          level: STATUS_INDICATORS.WARNING,
        });
      });
  }

  return {
    status: readonly(status),
    updateStatus,
    statusOptions: readonly(statusOptions),
  };
}
