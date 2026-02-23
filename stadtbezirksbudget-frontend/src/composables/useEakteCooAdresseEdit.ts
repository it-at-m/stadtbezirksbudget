import type { MaybeRefOrGetter } from "vue";

import { computed, ref, toValue } from "vue";

import { updateAntragReference } from "@/api/update-antragReference.ts";
import { STATUS_INDICATORS } from "@/constants.ts";
import { useSnackbarStore } from "@/stores/useSnackbarStore.ts";

const cooRegex = /^COO\.\d{4}\.\d{4}\.\d\.\d{7}$/;

/**
 * A custom composition function for editing the COO address of an Antrag.
 * It provides the necessary reactive state and methods to handle the editing process.
 *
 * @param antragId - The ID of the Antrag to update.
 * @param initialEakteCooAdresse - The initial COO address value.
 * @param onSave - A callback function to execute after saving the updated COO address.
 * @returns An object containing reactive properties and methods for managing the COO address edit.
 */
export function useEakteCooAdresseEdit(
  antragId: MaybeRefOrGetter<string>,
  initialEakteCooAdresse: MaybeRefOrGetter<string>,
  onSave: () => void
) {
  const snackbarStore = useSnackbarStore();
  const menu = ref<boolean>(false);
  const eakteCooAdresse = ref(toValue(initialEakteCooAdresse));
  const isValid = ref<boolean>(false);
  const isDirty = computed(
    () => eakteCooAdresse.value !== toValue(initialEakteCooAdresse)
  );
  const isSaveable = computed(() => isValid.value && isDirty.value);

  /**
   * Saves the updated COO address and shows a success or error message based on the outcome.
   */
  function save() {
    if (!isSaveable.value) return;
    updateAntragReference(toValue(antragId), {
      eakteCooAdresse: eakteCooAdresse.value,
    })
      .then(() => {
        snackbarStore.showMessage({
          message: "COO-Adresse aktualisiert",
          level: STATUS_INDICATORS.SUCCESS,
        });
        menu.value = false;
        onSave();
      })
      .catch(() => {
        snackbarStore.showMessage({
          message: "Fehler beim Aktualisieren der COO-Adresse",
          level: STATUS_INDICATORS.WARNING,
        });
      });
  }

  /**
   * Cancels the editing of the COO address and closes the edit menu.
   */
  function cancel() {
    eakteCooAdresse.value = toValue(initialEakteCooAdresse);
    menu.value = false;
  }

  /**
   * Validates the COO address format.
   * @param value - The COO address to validate.
   * @returns Returns true if valid, or an error message if invalid.
   */
  function validateCoo(value: string) {
    if (value && !cooRegex.test(value))
      return 'Muss dem Format "COO.XXXX.XXXX.X.XXXXXXX" entsprechen';
    return true;
  }

  return {
    // Controls the visibility of the edit menu
    menu,
    // COO address being edited
    eakteCooAdresse,
    // Indicates if the COO address is valid
    isValid,
    // Indicates if changes are saveable
    isSaveable,
    save,
    cancel,
    validateCoo,
  };
}
