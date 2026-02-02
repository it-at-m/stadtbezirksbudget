import type { Ref } from "vue";

export function useEakteList(
  emit: (
    e: "update:aktenzeichen",
    c: "update:cooAdresse",
    value: string | null
  ) => void,
  aktenzeichenMenu: Ref<boolean>,
  aktenzeichenNumberValue: Ref<string | null>,
  aktenzeichenMenuActivator: Ref<HTMLElement | null>,
  cooMenu: Ref<boolean>,
  cooNumberValue: Ref<string | null>,
  cooMenuActivator: Ref<HTMLElement | null>
) {
  function openAktenzeichen(e: MouseEvent): boolean {
    const el = e.currentTarget;
    if (!(el instanceof HTMLElement)) return false;
    aktenzeichenMenuActivator.value = el;
    aktenzeichenMenu.value = true;
    return true;
  }

  function aktenzeichenSaveNumber() {
    emit(
      "update:aktenzeichen",
      "update:cooAdresse",
      aktenzeichenNumberValue.value
    );
    aktenzeichenMenu.value = false;
  }

  function openCooAdresse(e: MouseEvent): boolean {
    const el = e.currentTarget;
    if (!(el instanceof HTMLElement)) return false;
    cooMenuActivator.value = el;
    cooMenu.value = true;
    return true;
  }

  function cooSaveNumber() {
    emit("update:aktenzeichen", "update:cooAdresse", cooNumberValue.value);
    cooMenu.value = false;
  }

  return {
    openAktenzeichen,
    aktenzeichenSaveNumber: aktenzeichenSaveNumber,
    openCooAdresse,
    cooSaveNumber,
  };
}
