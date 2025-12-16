import { createPinia, setActivePinia } from "pinia";
import { beforeEach, describe, expect, test } from "vitest";

import { useAntragListSortingStore } from "@/stores/useAntragListSortingStore";
import { createEmptyListSort } from "@/types/AntragListSort";

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

  test("initially empty store", () => {
    const store = useAntragListSortingStore();
    expect(store.sorting).toStrictEqual(createEmptyListSort());
  });

  test("setSorting stores sorting", () => {
    const store = useAntragListSortingStore();
    store.setSorting(testSorting);
    expect(store.sorting).toStrictEqual(testSorting);
  });
});
