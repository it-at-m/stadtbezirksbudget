import { createPinia, setActivePinia } from "pinia";
import { beforeEach, describe, expect, test } from "vitest";

import { STATUS_INDICATORS } from "@/constants";
import { useSnackbarStore } from "@/stores/useSnackbarStore.ts";

describe("snackbar store", () => {
  beforeEach(() => {
    setActivePinia(createPinia());
  });

  test("testInitialState", () => {
    const store = useSnackbarStore();
    expect(store.message).toBeUndefined();
    expect(store.level).toBe(STATUS_INDICATORS.INFO);
    expect(store.show).toBe(false);
  });

  test("testShowMessageSetsMessageLevelAndShowTrue", () => {
    const store = useSnackbarStore();
    store.showMessage({
      message: "Hallo Welt",
      level: STATUS_INDICATORS.SUCCESS,
    });
    expect(store.message).toBe("Hallo Welt");
    expect(store.level).toBe(STATUS_INDICATORS.SUCCESS);
    expect(store.show).toBe(true);
  });

  test("testShowMessageDefaultsLevelToInfo", () => {
    const store = useSnackbarStore();
    store.showMessage({ message: "Nur Info" });
    expect(store.message).toBe("Nur Info");
    expect(store.level).toBe(STATUS_INDICATORS.INFO);
    expect(store.show).toBe(true);
  });

  test("testUpdateShowChangesShowFlag", () => {
    const store = useSnackbarStore();
    store.showMessage({ message: "X" });
    expect(store.show).toBe(true);
    store.updateShow(false);
    expect(store.show).toBe(false);
    store.updateShow(true);
    expect(store.show).toBe(true);
  });

  test("testMultipleShowMessageCallsOverwritePrevious", () => {
    const store = useSnackbarStore();
    store.showMessage({ message: "erste" });
    expect(store.message).toBe("erste");
    store.showMessage({ message: "zweite", level: STATUS_INDICATORS.ERROR });
    expect(store.message).toBe("zweite");
    expect(store.level).toBe(STATUS_INDICATORS.ERROR);
    expect(store.show).toBe(true);
  });

  test("testShowMessageWithEmptyPayloadShowsUndefinedMessage", () => {
    const store = useSnackbarStore();
    store.showMessage({});
    expect(store.message).toBeUndefined();
    expect(store.level).toBe(STATUS_INDICATORS.INFO);
    expect(store.show).toBe(true);
  });
});
