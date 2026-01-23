import type { AntragDetails } from "@/types/antragDetails/AntragDetails.ts";

import { onMounted, ref } from "vue";

import { getAntragDetails } from "@/api/fetch-antragDetails.ts";
import { ROUTES_HOME, STATUS_INDICATORS } from "@/constants.ts";
import router from "@/plugins/router.ts";
import { useSnackbarStore } from "@/stores/useSnackbarStore.ts";

export function useAntragDetails(id: string) {
  const snackbarStore = useSnackbarStore();

  const details = ref<AntragDetails>();

  function fetchDetails(id: string) {
    getAntragDetails(id)
      .then((content: AntragDetails) => {
        details.value = content;
      })
      .catch(() => {
        snackbarStore.showMessage({
          message: "Fehler beim Laden des Antrags",
          level: STATUS_INDICATORS.WARNING,
        });
        router.push({ name: ROUTES_HOME }).then();
      });
  }

  onMounted(() => {
    fetchDetails(id);
  });

  return { details };
}
