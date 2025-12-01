import { mount } from "@vue/test-utils";
import { beforeEach, describe, expect, test, vi } from "vitest";
import { nextTick } from "vue";

import { getAntragListFilterOptions } from "@/api/fetch-antragListFilterOptions";
import { useInitializeStores } from "@/composables/initializeStores";
import { STATUS_INDICATORS } from "@/constants";
import { useAntragListFilterOptionsStore } from "@/stores/antragListFilterOptions";
import { useSnackbarStore } from "@/stores/snackbar";

vi.mock("@/api/fetch-antragListFilterOptions", () => {
  const getAntragListFilterOptions = vi.fn();
  return { getAntragListFilterOptions };
});
vi.mock("@/stores/antragListFilterOptions", () => {
  const setFilterOptions = vi.fn();
  return { useAntragListFilterOptionsStore: () => ({ setFilterOptions }) };
});
vi.mock("@/stores/snackbar", () => {
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
  });

  describe("AntragListFilterOptionsStore initialization", () => {
    test("calls fetch and sets store on success", async () => {
      const apiMock = getAntragListFilterOptions as unknown as vi.Mock;
      const setFilterOptions = useAntragListFilterOptionsStore()
        .setFilterOptions as vi.Mock;

      const apiResult = { some: "data" } as unknown;
      apiMock.mockResolvedValue(apiResult);

      mount(Comp);

      await Promise.resolve();
      await nextTick();

      expect(apiMock).toHaveBeenCalled();
      expect(setFilterOptions).toHaveBeenCalledWith(apiResult);
    });

    test("shows snackbar with error.message if API throws error with message", async () => {
      const apiMock = getAntragListFilterOptions as unknown as vi.Mock;
      const showMessage = useSnackbarStore().showMessage as vi.Mock;
      const error = new Error("network error");
      apiMock.mockRejectedValue(error);

      mount(Comp);

      await Promise.resolve();
      await nextTick();

      expect(showMessage).toHaveBeenCalledWith({
        message: error.message,
        level: STATUS_INDICATORS.WARNING,
      });
    });

    test("shows snackbar with fallback message if error has no message", async () => {
      const apiMock = getAntragListFilterOptions as unknown as vi.Mock;
      const showMessage = useSnackbarStore().showMessage as vi.Mock;
      apiMock.mockRejectedValue({});

      mount(Comp);

      await Promise.resolve();
      await nextTick();

      expect(showMessage).toHaveBeenCalledWith({
        message: "Fehler beim Laden der Filteroptionen",
        level: STATUS_INDICATORS.WARNING,
      });
    });
  });
});
