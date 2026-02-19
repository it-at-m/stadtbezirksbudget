import type { MaybeRefOrGetter } from "vue";

import { computed, ref, toValue } from "vue";

import { updateAntragReference } from "@/api/update-antragReference.ts";
import { STATUS_INDICATORS } from "@/constants.ts";
import { useSnackbarStore } from "@/stores/useSnackbarStore.ts";

const cooRegex = /^COO\.\d{4}\.\d{4}\.\d\.\d{7}$/;

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
    () => eakteCooAdresse.value != toValue(initialEakteCooAdresse)
  );
  const isSaveable = computed(() => isValid.value && isDirty.value);

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

  function cancel() {
    menu.value = false;
  }

  function validateCoo(value: string) {
    if (!value) return true;
    if (!cooRegex.test(value))
      return 'Muss dem Format "COO.XXXX.XXXX.X.XXXXXXX" entsprechen';
    return true;
  }

  return {
    menu,
    eakteCooAdresse,
    isValid,
    isSaveable,
    save,
    cancel,
    validateCoo,
  };
}
