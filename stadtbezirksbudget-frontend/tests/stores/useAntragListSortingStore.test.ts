import { createPinia, setActivePinia } from "pinia";
import { beforeEach, describe, expect, test } from "vitest";

import { useAntragListSortingStore } from "@/stores/useAntragListSortingStore";
import {
  createDefaultListSort,
  createEmptyListSort,
} from "@/types/AntragListSort";

const testSorting = {
  ...createEmptyListSort(),
  test: {
    title: "Test",
    sortBy: "test",
    sortDirection: "asc",
  },
};

describe("useAntragListSortingStore", () => {
  beforeEach(() => {
    setActivePinia(createPinia());
  });

  test("initially default store", () => {
    const store = useAntragListSortingStore();
    expect(store.sorting).toStrictEqual(createDefaultListSort());
  });

  test("setSorting stores sorting", () => {
    const store = useAntragListSortingStore();
    store.setSorting(testSorting);
    expect(store.sorting).toStrictEqual(testSorting);
  });
});
