import type AntragSummary from "@/types/AntragSummary.ts";
import type Page from "@/types/Page.ts";

import { beforeEach, describe, expect, test, vi } from "vitest";

import { getAntragsSummaryList } from "@/api/fetch-antragSummary-list.ts";
import { useAntragSummaryList } from "@/composables/useAntragSummaryList";
import { STATUS_INDICATORS } from "@/constants.ts";
import { useAntragListFilterStore } from "@/stores/useAntragListFilterStore.ts";
import { useSnackbarStore } from "@/stores/useSnackbarStore.ts";
import {
  AntragListFilter,
  defaultAntragListFilter,
} from "@/types/AntragListFilter.ts";
import { useAntragListSortingStore } from "../../src/stores/useAntragListSortingStore";
import {
  AntragListSort,
  createEmptyListSort,
} from "../../src/types/AntragListSort";

vi.mock("@/api/fetch-antragSummary-list.ts");
vi.mock("@/stores/useSnackbarStore.ts");
vi.mock("@/stores/useAntragListFilterStore.ts");
vi.mock("@/stores/useAntragListSortingStore.ts");

describe("useAntragSummaryList", () => {
  let snackbarStoreMock: { showMessage: ReturnType<typeof vi.fn> };
  let filterStoreMock: {
    filters: AntragListFilter;
    setFilters: ReturnType<typeof vi.fn>;
    $subscribe: ReturnType<typeof vi.fn>;
  };
  let sortingStoreMock: {
    sorting: AntragListSort;
    setListSorting: ReturnType<typeof vi.fn>;
    $subscribe: ReturnType<typeof vi.fn>;
  };
  const filtersValue = defaultAntragListFilter();
  const sortingValue = createEmptyListSort();

  beforeEach(() => {
    snackbarStoreMock = {
      showMessage: vi.fn(),
    };
    filterStoreMock = {
      filters: filtersValue,
      setFilters: vi.fn(),
      $subscribe: vi.fn(),
    };
    sortingStoreMock = {
      sorting: sortingValue,
      setListSorting: vi.fn(),
      $subscribe: vi.fn(),
    };

    (useSnackbarStore as vi.Mock).mockReturnValue(snackbarStoreMock);
    (useAntragListFilterStore as vi.Mock).mockReturnValue(filterStoreMock);
    (useAntragListSortingStore as vi.Mock).mockReturnValue(sortingStoreMock);
  });

  describe("fetchItems", () => {
    test("fetches items successfully", async () => {
      const mockResponse: Page<AntragSummary> = {
        content: [
          {
            id: "2",
            status: "IN_BEARBEITUNG",
            zammadNr: "Z-002",
            aktenzeichen: "AZ-002",
            bezirksausschussNr: 2,
            eingangDatum: "2025-01-16",
            antragstellerName: "Test Antragsteller 2",
            projektTitel: "Test Projekt 2",
            beantragtesBudget: 7500,
            istFehlbetrag: false,
            aktualisierung: "Aktualisiert",
            aktualisierungDatum: "2025-01-16",
          },
        ],
        page: { size: 5, number: 1, totalElements: 1, totalPages: 1 },
      };

      (getAntragsSummaryList as vi.Mock).mockResolvedValue(mockResponse);
      const { items, totalItems, fetchItems, loading } = useAntragSummaryList();

      fetchItems();

      await vi.waitFor(() => {
        expect(items.value).toEqual(mockResponse.content);
        expect(totalItems.value).toBe(mockResponse.page.totalElements);
        expect(loading.value).toBe(false);
      });
    });

    test("handles api errors when fetching items", async () => {
      const errorMessage = "API Error";
      (getAntragsSummaryList as vi.Mock).mockRejectedValue(
        new Error(errorMessage)
      );

      const { fetchItems, loading } = useAntragSummaryList();
      fetchItems();

      await vi.waitFor(() => {
        expect(snackbarStoreMock.showMessage).toHaveBeenCalledWith({
          message: errorMessage,
          level: STATUS_INDICATORS.WARNING,
        });
        expect(loading.value).toBe(false);
      });
    });

    test("handles generic errors when fetching items", async () => {
      (getAntragsSummaryList as vi.Mock).mockRejectedValue(new Error());

      const { fetchItems } = useAntragSummaryList();
      fetchItems();

      await vi.waitFor(() => {
        expect(snackbarStoreMock.showMessage).toHaveBeenCalledWith({
          message: "Fehler beim Laden der Anträge",
          level: STATUS_INDICATORS.WARNING,
        });
      });
    });
  });

  describe("updateOptions", () => {
    test("fetches new items when updating options", async () => {
      const mockResponse: Page<AntragSummary> = {
        content: [
          {
            id: "1",
            status: "EINGEGANGEN",
            zammadNr: "Z-001",
            aktenzeichen: "AZ-001",
            bezirksausschussNr: 1,
            eingangDatum: "2025-01-15",
            antragstellerName: "Test Antragsteller",
            projektTitel: "Test Projekt",
            beantragtesBudget: 5000,
            istFehlbetrag: false,
            aktualisierung: "Erstellt",
            aktualisierungDatum: "2025-01-15",
          },
        ],
        page: { size: 10, number: 0, totalElements: 1, totalPages: 1 },
      };

      (getAntragsSummaryList as vi.Mock).mockResolvedValue(mockResponse);
      const { updateOptions, items } = useAntragSummaryList();

      updateOptions({ page: 2, itemsPerPage: 5 });

      await vi.waitFor(() => {
        expect(getAntragsSummaryList).toHaveBeenCalledWith(
          1,
          5,
          filtersValue,
          createEmptyListSort()
        );
        expect(items.value).toEqual(mockResponse.content);
      });
    });

    test("updates options correctly", () => {
      const { updateOptions, page, itemsPerPage } = useAntragSummaryList();

      updateOptions({ page: 3, itemsPerPage: 15 });

      expect(page.value).toBe(3);
      expect(itemsPerPage.value).toBe(15);
    });
  });

  test("handles filter store subscription", async () => {
    let capturedSubscribe: (() => void) | undefined;
    filterStoreMock.$subscribe = vi.fn((cb: () => void) => {
      capturedSubscribe = cb;
    });

    const mockResponse: Page<AntragSummary> = {
      content: [],
      page: { size: 10, number: 0, totalElements: 0, totalPages: 0 },
    };
    (getAntragsSummaryList as vi.Mock).mockResolvedValue(mockResponse);

    const { page, updateOptions } = useAntragSummaryList();

    updateOptions({ page: 5, itemsPerPage: 10 });
    expect(page.value).toBe(5);

    capturedSubscribe?.();

    await vi.waitFor(() => {
      expect(page.value).toBe(1);
      expect(getAntragsSummaryList).toHaveBeenCalled();
    });
  });

  test("handles sorting store subscription", async () => {
    let capturedSubscribe: (() => void) | undefined;
    sortingStoreMock.$subscribe = vi.fn((cb: () => void) => {
      capturedSubscribe = cb;
    });

    const mockResponse: Page<AntragSummary> = {
      content: [],
      page: { size: 10, number: 0, totalElements: 0, totalPages: 0 },
    };
    (getAntragsSummaryList as vi.Mock).mockResolvedValue(mockResponse);

    const { page, updateOptions } = useAntragSummaryList();

    updateOptions({ page: 5, itemsPerPage: 10 });
    expect(page.value).toBe(5);

    capturedSubscribe?.();

    await vi.waitFor(() => {
      expect(page.value).toBe(1);
      expect(getAntragsSummaryList).toHaveBeenCalled();
    });
  });

  test("updating computed value sortBy updates store", () => {
    const { sortBy } = useAntragSummaryList();

    sortBy.value = [{ sortBy: "test", sortDesc: false }];

    expect(sortingStoreMock.setListSorting).toHaveBeenCalled();
  });

  test("getting computed value sortBy returns correct value", () => {
    const { sortBy } = useAntragSummaryList();
    const sortingGetter = vi.fn(() => createEmptyListSort());
    Object.defineProperty(sortingStoreMock, "sorting", {
      get: sortingGetter,
      configurable: true,
    });

    // eslint-disable-next-line @typescript-eslint/no-unused-expressions
    sortBy.value;

    expect(sortingGetter).toHaveBeenCalled();
  });
});
