import { createPinia, setActivePinia } from "pinia";
import { beforeEach, describe, expect, test } from "vitest";

import { useAntragListFilterStore } from "@/stores/useAntragListFilterStore.ts";
import {
  AntragListFilter,
  defaultAntragListFilter,
} from "@/types/AntragListFilter";

const testFilters: AntragListFilter = {
  status: ["EINGEGANGEN", "ABGESCHLOSSEN"],
  bezirksausschussNr: [1, 5],
  eingangDatum: [
    new Date("2025-11-26T00:00:00Z"),
    new Date("2025-11-27T00:00:00Z"),
    new Date("2025-11-28T00:00:00Z"),
  ],
  antragstellerName: "TEST_NAME",
  projektTitel: "TEST_TITEL",
  beantragtesBudgetVon: 537.25,
  beantragtesBudgetBis: 1098.98,
  finanzierungArt: "FEST",
  aktualisierungArt: ["E_AKTE"],
  aktualisierungDatum: [new Date("2025-11-25T00:00:00Z")],
};

describe("useAntragListFilterStore", () => {
  beforeEach(() => {
    setActivePinia(createPinia());
  });

  test("initially defaultAntragListFilter", () => {
    const store = useAntragListFilterStore();
    expect(store.filters).toStrictEqual(defaultAntragListFilter());
  });

  test("setFilters stores filter", () => {
    const store = useAntragListFilterStore();
    store.setFilters(testFilters);
    expect(store.filters).toStrictEqual(testFilters);
  });

  test("setSearch updates search", () => {
    const store = useAntragListFilterStore();
    const newSearch = "new search";
    store.setSearch(newSearch);
    expect(store.filters.search).toBe(newSearch);
  });
});
