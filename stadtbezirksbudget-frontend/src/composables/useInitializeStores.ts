import type { AntragListFilterOptions } from "@/types/AntragListFilter.ts";

import { onMounted } from "vue";

import { getAntragListFilterOptions } from "@/api/fetch-antragListFilterOptions.ts";
import { getFrontendConfig } from "@/api/fetch-frontendConfig.ts";
import { getUser } from "@/api/user-client.ts";
import { STATUS_INDICATORS } from "@/constants.ts";
import { useAntragListFilterOptionsStore } from "@/stores/useAntragListFilterOptionsStore.ts";
import { useConfigStore } from "@/stores/useConfigStore.ts";
import { useSnackbarStore } from "@/stores/useSnackbarStore.ts";
import { useUserStore } from "@/stores/useUserStore.ts";
import User, { UserLocalDevelopment } from "@/types/User.ts";

/**
 * Composable to initialize stores by fetching necessary data on component mount.
 */
export function useInitializeStores() {
  const snackbarStore = useSnackbarStore();
  const userStore = useUserStore();
  const antragListFilterOptionsStore = useAntragListFilterOptionsStore();
  const configStore = useConfigStore();

  function fetchFrontendConfig() {
    getFrontendConfig()
      .then((config) => configStore.setConfig(config))
      .catch(() => {
        snackbarStore.showMessage({
          message: "Fehler beim Laden der Frontend-Konfiguration",
          level: STATUS_INDICATORS.WARNING,
        });
      });
  }

  function fetchUser(): void {
    getUser()
      .then((user: User) => userStore.setUser(user))
      .catch(() => {
        if (import.meta.env.DEV) {
          userStore.setUser(UserLocalDevelopment());
        } else {
          userStore.setUser(null);
        }
      });
  }

  function fetchFilterOptions() {
    getAntragListFilterOptions()
      .then((options: AntragListFilterOptions) => {
        antragListFilterOptionsStore.setFilterOptions(options);
      })
      .catch(() => {
        snackbarStore.showMessage({
          message: "Fehler beim Laden der Filteroptionen",
          level: STATUS_INDICATORS.WARNING,
        });
      });
  }

  onMounted(() => {
    fetchUser();
    fetchFilterOptions();
    fetchFrontendConfig();
  });
}
