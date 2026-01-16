import { mount } from "@vue/test-utils";
import { beforeEach, describe, expect, test, vi } from "vitest";
import { nextTick } from "vue";

import { getAntragDetails } from "@/api/fetch-antragDetails.ts";
import { useAntragDetails } from "@/composables/useAntragDetails.ts";
import { STATUS_INDICATORS } from "@/constants";
import router from "@/plugins/router";
import { useSnackbarStore } from "@/stores/useSnackbarStore.ts";
import { ROUTES_HOME } from "../../src/constants";

vi.mock("@/api/fetch-antragDetails.ts");
vi.mock("@/stores/useSnackbarStore.ts", () => {
  const showMessage = vi.fn();
  return { useSnackbarStore: () => ({ showMessage }) };
});
vi.mock("@/plugins/router.ts", () => ({
  default: {
    push: vi.fn(() => Promise.resolve()),
  },
}));

describe("useAntragDetails", () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  test("calls fetch and sets details on success", async () => {
    const apiResult = { projektTitel: "Projekt" };
    (getAntragDetails as vi.Mock).mockResolvedValue(apiResult);

    let composable: typeof useAntragDetails;
    const antragId = "80000000-0000-0000-0000-000000000013";
    const component = {
      template: "<div/>",
      setup() {
        composable = useAntragDetails(antragId);
        return {};
      },
    };

    mount(component);

    await Promise.resolve();
    await nextTick();

    expect(getAntragDetails).toHaveBeenCalledWith(antragId);
    expect(composable.details.value).toEqual(apiResult);
  });

  test("shows error snackbar and routes to home if API throws error", async () => {
    const error = new Error("network error");
    (getAntragDetails as vi.Mock).mockRejectedValue(error);

    const antragId = "80000000-0000-0000-0000-000000000013";
    let composable: typeof useAntragDetails;
    const component = {
      template: "<div/>",
      setup() {
        composable = useAntragDetails(antragId);
        return {};
      },
    };

    mount(component);

    await Promise.resolve();
    await nextTick();

    expect(getAntragDetails).toHaveBeenCalledWith(antragId);
    expect(useSnackbarStore().showMessage).toHaveBeenCalledWith({
      message: "Fehler beim Laden des Antrags",
      level: STATUS_INDICATORS.WARNING,
    });
    expect(composable.details.value).toBeUndefined();
    expect(router.push).toHaveBeenCalledWith({
      name: ROUTES_HOME,
    });
  });
});
