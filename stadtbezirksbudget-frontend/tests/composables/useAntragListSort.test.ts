import type { DataTableSortItem } from "vuetify";

import { beforeEach, describe, expect, test, vi } from "vitest";
import { reactive } from "vue";

import { useAntragListSort } from "@/composables/useAntragListSort";
import { useAntragListSortingStore } from "@/stores/useAntragListSortingStore";
import {
  AntragListSort,
  AntragListSortOption,
  createDefaultListSort,
  createEmptyListSort,
} from "@/types/AntragListSort";

vi.mock("@/stores/useAntragListSortingStore.ts");

const testSorting: AntragListSort = createEmptyListSort();
const testEmptySortingItems: DataTableSortItem[] = [];
const testSortingItems: DataTableSortItem[] = [
  {
    key: "status",
    order: "asc",
  },
];

describe("useAntragListSort", () => {
  let sortingStoreMock: {
    sorting: AntragListSort;
    setSorting: ReturnType<typeof vi.fn>;
  };

  beforeEach(() => {
    sortingStoreMock = reactive({
      sorting: testSorting,
      setSorting: vi.fn((payload: AntragListSort) => {
        sortingStoreMock.sorting = payload;
      }),
    });
    (useAntragListSortingStore as vi.Mock).mockReturnValue(sortingStoreMock);
  });

  test("gets initial sorting from store", () => {
    const { sorting } = useAntragListSort();
    expect(sorting.value).toStrictEqual(testSorting);
  });

  test("resets sorting to empty and updates store", () => {
    const { resetSorting, sorting } = useAntragListSort();

    resetSorting();
    expect(sorting.value).toStrictEqual(createDefaultListSort());
    expect(sortingStoreMock.setSorting).toHaveBeenCalledWith(
      createDefaultListSort()
    );
  });

  test("update sorting updates store", () => {
    const { updateSorting } = useAntragListSort();

    const newSortOption: AntragListSortOption = {
      sortBy: "test",
      sortDirection: "asc",
      title: "Test",
    };

    updateSorting(newSortOption);
    expect(sortingStoreMock.setSorting).toHaveBeenCalledWith({
      ...createEmptyListSort(),
      [newSortOption.sortBy]: newSortOption,
    });
  });

  test("update sorting without new sort item clears store", () => {
    const { updateSortingWithSortItem } = useAntragListSort();

    updateSortingWithSortItem(testEmptySortingItems);
    expect(sortingStoreMock.setSorting).toHaveBeenCalledWith(
      createEmptyListSort()
    );
  });

  test("update sorting with new sort item updates store", () => {
    const { updateSortingWithSortItem } = useAntragListSort();

    updateSortingWithSortItem(testSortingItems);
    expect(sortingStoreMock.setSorting).toHaveBeenCalledWith(
      expect.objectContaining({
        status: expect.objectContaining({
          sortBy: "status",
          sortDirection: "asc",
        }),
      })
    );
  });
});
