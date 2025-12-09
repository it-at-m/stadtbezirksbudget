import { setActivePinia } from "pinia";
import { beforeEach, describe, expect, test } from "vitest";

import { STATUS_INDICATORS } from "@/constants";
import pinia from "@/plugins/pinia.ts";
import { useSnackbarStore } from "@/stores/useSnackbarStore.ts";

describe("useSnackbarStore", () => {
  beforeEach(() => {
    setActivePinia(pinia);
  });

  test("has initial state", () => {
    const store = useSnackbarStore();
    expect(store.message).toBeUndefined();
    expect(store.level).toBe(STATUS_INDICATORS.INFO);
    expect(store.show).toBe(false);
  });

  describe("showMessage", () => {
    test("shows message with correct level", () => {
      const store = useSnackbarStore();
      store.showMessage({
        message: "Hallo Welt",
        level: STATUS_INDICATORS.SUCCESS,
      });
      expect(store.message).toBe("Hallo Welt");
      expect(store.level).toBe(STATUS_INDICATORS.SUCCESS);
      expect(store.show).toBe(true);
    });

    test("shows message with default level", () => {
      const store = useSnackbarStore();
      store.showMessage({ message: "Nur Info" });
      expect(store.message).toBe("Nur Info");
      expect(store.level).toBe(STATUS_INDICATORS.INFO);
      expect(store.show).toBe(true);
    });

    test("new message replaces previous one", () => {
      const store = useSnackbarStore();
      store.showMessage({ message: "erste" });
      expect(store.message).toBe("erste");
      store.showMessage({ message: "zweite", level: STATUS_INDICATORS.ERROR });
      expect(store.message).toBe("zweite");
      expect(store.level).toBe(STATUS_INDICATORS.ERROR);
      expect(store.show).toBe(true);
    });

    test("shows message with empty payload", () => {
      const store = useSnackbarStore();
      store.showMessage({});
      expect(store.message).toBeUndefined();
      expect(store.level).toBe(STATUS_INDICATORS.INFO);
      expect(store.show).toBe(true);
    });
  });

  describe("updateShow", () => {
    test("updates show flag", () => {
      const store = useSnackbarStore();
      store.showMessage({ message: "X" });
      expect(store.show).toBe(true);
      store.updateShow(false);
      expect(store.show).toBe(false);
      store.updateShow(true);
      expect(store.show).toBe(true);
    });
  });
});
