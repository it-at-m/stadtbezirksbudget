import { createPinia, setActivePinia } from "pinia";
import { beforeEach, describe, expect, test } from "vitest";

import { useAntragListFilterOptionsStore } from "@/stores/useAntragListFilterOptionsStore.ts";
import { AntragListFilterOptions } from "@/types/AntragListFilter.ts";

describe("useAntragListFilterOptionsStore", () => {
  beforeEach(() => {
    setActivePinia(createPinia());
  });

  test("contains initially empty arrays", () => {
    const store = useAntragListFilterOptionsStore();
    expect(store.filterOptions).toStrictEqual({
      antragstellerNamen: [],
      projektTitel: [],
    });
  });

  test("setFilterOptions stores filter", () => {
    const testOptions: AntragListFilterOptions = {
      antragstellerNamen: ["TEST_NAME_1", "TEST_NAME_2"],
      projektTitel: ["TEST_TITLE_1", "TEST_TITLE_2", "TEST_TITLE_3"],
    };
    const store = useAntragListFilterOptionsStore();
    store.setFilterOptions(testOptions);
    expect(store.filterOptions).toStrictEqual(testOptions);
  });
});
