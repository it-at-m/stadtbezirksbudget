import type { Ref } from "vue";

import { ref } from "vue";

import { updateAntragAktenzeichen } from "@/api/update-antrag-aktenzeichen.ts";
import { STATUS_INDICATORS } from "@/constants.ts";
import { useSnackbarStore } from "@/stores/useSnackbarStore.ts";

export function useAntragAktenzeichenUpdate(
  antragId: Ref<string>,
  initialAktenzeichen: Ref<string>
) {
  const snackbarStore = useSnackbarStore();

  const aktenzeichen = ref<string>(initialAktenzeichen.value);
  const oldAktenzeichen = ref<string>(initialAktenzeichen.value);

  /**
   * Updates the status of the Antrag both locally and in the backend.
   * @param newStatus - The new status to set for the Antrag.
   */
  function updateAktenzeichen(newAktenzeichen: string) {
    if (!newAktenzeichen) return;
    updateAntragAktenzeichen(antragId.value, newAktenzeichen)
      .then(() => {
        aktenzeichen.value = newAktenzeichen;
        oldAktenzeichen.value = newAktenzeichen;
        snackbarStore.showMessage({
          message: "Aktenzeichen aktualisiert",
          level: STATUS_INDICATORS.SUCCESS,
        });
      })
      .catch(() => {
        aktenzeichen.value = oldAktenzeichen.value;
        snackbarStore.showMessage({
          message: "Fehler beim Aktualisieren des Aktenzeichen",
          level: STATUS_INDICATORS.WARNING,
        });
      })
      .finally(() => {
        (document.activeElement as HTMLElement)?.blur();
      });
  }
  return {
    updateAktenzeichen,
    aktenzeichen,
  };
}
