import { beforeEach, describe, expect, test, vi } from "vitest";

import { useAntragListFilter } from "@/composables/useAntragListFilter";
import { useAntragListFilterStore } from "@/stores/useAntragListFilterStore.ts";
import {
  AntragListFilter,
  defaultAntragListFilter,
} from "@/types/AntragListFilter";

vi.mock("@/stores/useAntragListFilterStore.ts");

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
  art: "Fest",
  aktualisierungArt: ["E_AKTE"],
  aktualisierungDatum: [new Date("2025-11-25T00:00:00Z")],
};

describe("useAntragListFilter", () => {
  let filterStoreMock;

  beforeEach(() => {
    filterStoreMock = { filters: testFilters, setFilters: vi.fn() };
    (useAntragListFilterStore as vi.Mock).mockReturnValue(filterStoreMock);
  });

  test("gets initial filters from store", () => {
    const { filters } = useAntragListFilter();
    expect(filters.value).toStrictEqual(testFilters);
  });

  describe("resetFilters", () => {
    test("resets filters to empty and updates store", async () => {
      const { resetFilters, filters } = useAntragListFilter();

      resetFilters();
      expect(filters.value).toStrictEqual(defaultAntragListFilter());
      expect(filterStoreMock.setFilters).toHaveBeenCalled();
    });
  });

  describe("updateFilters", () => {
    test("update filters updates store on change", async () => {
      const { updateFilters, filters } = useAntragListFilter();

      updateFilters();
      expect(filterStoreMock.setFilters).not.toHaveBeenCalled();

      filters.value = defaultAntragListFilter();
      updateFilters();
      expect(filterStoreMock.setFilters).toHaveBeenCalled();
    });
  });
});
