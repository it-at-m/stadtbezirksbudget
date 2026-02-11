import { mount } from "@vue/test-utils";
import { beforeEach, describe, expect, test, vi } from "vitest";
import { nextTick } from "vue";

import { getAntragListFilterOptions } from "@/api/fetch-antragListFilterOptions";
import { getFrontendConfig } from "@/api/fetch-frontendConfig.ts";
import { getUser } from "@/api/user-client.ts";
import { useInitializeStores } from "@/composables/useInitializeStores.ts";
import { STATUS_INDICATORS } from "@/constants";
import { useAntragListFilterOptionsStore } from "@/stores/useAntragListFilterOptionsStore.ts";
import { useConfigStore } from "@/stores/useConfigStore.ts";
import { useSnackbarStore } from "@/stores/useSnackbarStore.ts";
import { useUserStore } from "@/stores/useUserStore.ts";
import { UserLocalDevelopment } from "@/types/User.ts";

vi.mock("@/api/fetch-antragListFilterOptions", () => {
  const getAntragListFilterOptions = vi.fn();
  return { getAntragListFilterOptions };
});
vi.mock("@/stores/useAntragListFilterOptionsStore.ts", () => {
  const setFilterOptions = vi.fn();
  return { useAntragListFilterOptionsStore: () => ({ setFilterOptions }) };
});
vi.mock("@/api/user-client.ts", () => {
  const getUser = vi.fn();
  return { getUser };
});
vi.mock("@/stores/useUserStore.ts", () => {
  const setUser = vi.fn();
  return { useUserStore: () => ({ setUser }) };
});
vi.mock("@/api/fetch-frontendConfig", () => {
  const getFrontendConfig = vi.fn();
  return { getFrontendConfig };
});
vi.mock("@/stores/useConfigStore.ts", () => {
  const setConfig = vi.fn();
  return { useConfigStore: () => ({ setConfig }) };
});
vi.mock("@/stores/useSnackbarStore.ts", () => {
  const showMessage = vi.fn();
  return { useSnackbarStore: () => ({ showMessage }) };
});

const Comp = {
  template: "<div/>",
  setup() {
    useInitializeStores();
    return {};
  },
};

describe("useInitializeStores", () => {
  beforeEach(() => {
    vi.clearAllMocks();
    getAntragListFilterOptions.mockResolvedValue({});
    getUser.mockResolvedValue({});
    getFrontendConfig.mockResolvedValue({});
  });

  describe("AntragListFilterOptionsStore initialization", () => {
    test("calls fetch and sets store on success", async () => {
      const apiResult = { some: "data" };
      getAntragListFilterOptions.mockResolvedValue(apiResult);

      mount(Comp);

      await Promise.resolve();
      await nextTick();

      expect(getAntragListFilterOptions).toHaveBeenCalled();
      expect(
        useAntragListFilterOptionsStore().setFilterOptions
      ).toHaveBeenCalledWith(apiResult);
    });

    test("shows snackbar with error message if API throws error ", async () => {
      const error = new Error("network error");
      getAntragListFilterOptions.mockRejectedValue(error);

      mount(Comp);

      await Promise.resolve();
      await nextTick();

      expect(useSnackbarStore().showMessage).toHaveBeenCalledWith({
        message: "Fehler beim Laden der Filteroptionen",
        level: STATUS_INDICATORS.WARNING,
      });
    });
  });

  describe("UserStore initialization", () => {
    test("calls fetch and sets store on success", async () => {
      const apiResult = { some: "data" };
      getUser.mockResolvedValue(apiResult);

      mount(Comp);

      await Promise.resolve();
      await nextTick();

      expect(getUser).toHaveBeenCalled();
      expect(useUserStore().setUser).toHaveBeenCalledWith(apiResult);
    });

    test("sets local development user on api error and dev env", async () => {
      getUser.mockRejectedValue(new Error("API Error"));
      vi.stubEnv("DEV", true);

      mount(Comp);

      await Promise.resolve();
      await nextTick();

      expect(getUser).toHaveBeenCalled();
      expect(useUserStore().setUser).toHaveBeenCalledWith(
        UserLocalDevelopment()
      );
    });

    test("sets null user on api error and no dev env", async () => {
      getUser.mockRejectedValue(new Error("API Error"));
      vi.stubEnv("DEV", false);

      mount(Comp);

      await Promise.resolve();
      await nextTick();

      expect(getUser).toHaveBeenCalled();
      expect(useUserStore().setUser).toHaveBeenCalledWith(null);
    });
  });

  describe("FrontendConfigStore initialization", () => {
    test("calls fetch and sets store on success", async () => {
      const apiResult = { some: "data" };
      getFrontendConfig.mockResolvedValue(apiResult);

      mount(Comp);

      await Promise.resolve();
      await nextTick();

      expect(getFrontendConfig).toHaveBeenCalled();
      expect(useConfigStore().setConfig).toHaveBeenCalledWith(apiResult);
    });

    test("shows snackbar with error message if API throws error ", async () => {
      const error = new Error("network error");
      getFrontendConfig.mockRejectedValue(error);

      mount(Comp);

      await Promise.resolve();
      await nextTick();

      expect(useSnackbarStore().showMessage).toHaveBeenCalledWith({
        message: "Fehler beim Laden der Frontend-Konfiguration",
        level: STATUS_INDICATORS.WARNING,
      });
    });
  });
});
