import { beforeEach, describe, expect, test, vi } from "vitest";
import { DataTableSortItem } from "vuetify/framework";

import { useAntragListSort } from "../../src/composables/useAntragListSort";
import { useAntragListSortingStore } from "../../src/stores/useAntragListSortingStore";
import {
  AntragListSort,
  AntragListSortOption,
  createEmptyListSort,
} from "../../src/types/AntragListSort";

vi.mock("@/stores/useAntragListSortingStore.ts");

const testSorting: AntragListSort = {};
const testEmptySortingItems: DataTableSortItem[] = [];
const testSortingItems: DataTableSortItem[] = [
  {
    key: "status",
    order: "asc",
  },
];

describe("useAntragListSort", () => {
  let sortingStoreMock;

  beforeEach(() => {
    sortingStoreMock = { sorting: testSorting, setListSorting: vi.fn() };
    (useAntragListSortingStore as vi.Mock).mockReturnValue(sortingStoreMock);
  });

  test("gets initial sorting from store", () => {
    const { sortOption } = useAntragListSort();
    expect(sortOption.value).toStrictEqual(testSorting);
  });

  test("resets sorting to empty and updates store", async () => {
    const { resetSorting, sortOption } = useAntragListSort();

    resetSorting();
    expect(sortOption.value).toStrictEqual(createEmptyListSort());
    expect(sortingStoreMock.setListSorting).toHaveBeenCalled();
  });

  test("update sorting updates store", async () => {
    const { updateSorting, sortOption } = useAntragListSort();

    const newSortOption: AntragListSortOption = {
      sortBy: "test",
      sortDirection: "asc",
      title: "Test",
    };

    updateSorting(newSortOption);
    expect(sortingStoreMock.setListSorting).toHaveBeenCalled();
    expect(sortOption.value.test).toStrictEqual(newSortOption);
  });

  test("update sorting without new sort item resets store", async () => {
    const { updateSortingWithSortItem } = useAntragListSort();

    updateSortingWithSortItem(testEmptySortingItems);
    expect(sortingStoreMock.setListSorting).toHaveBeenCalledWith(
      createEmptyListSort()
    );
  });

  test("update sorting with new sort item updates store", async () => {
    const { updateSortingWithSortItem } = useAntragListSort();

    updateSortingWithSortItem(testSortingItems);
    expect(sortingStoreMock.setListSorting).not.toHaveBeenCalledWith(
      createEmptyListSort()
    );
  });
});
