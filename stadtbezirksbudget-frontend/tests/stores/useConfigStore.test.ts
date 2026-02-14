import { createPinia, setActivePinia } from "pinia";
import { beforeEach, describe, expect, test } from "vitest";

import { useConfigStore } from "@/stores/useConfigStore";

describe("useConfigStore", () => {
  beforeEach(() => {
    setActivePinia(createPinia());
  });

  test("initially null config", () => {
    const store = useConfigStore();
    expect(store.config).toBeNull();
    expect(store.getZammadBaseUrl).toBeUndefined();
    expect(store.getEakteBaseUrl).toBeUndefined();
  });

  test("setConfig stores config", () => {
    const config = {
      zammadBaseUrl: "https://zammad.muenchen.invalid",
      eakteBaseUrl: "https://akte.muenchen.invalid",
    };

    const store = useConfigStore();
    store.setConfig(config);
    expect(store.config).toStrictEqual(config);
    expect(store.getZammadBaseUrl).toBe("https://zammad.muenchen.invalid");
    expect(store.getEakteBaseUrl).toBe("https://akte.muenchen.invalid");
  });
});
