import type AntragSummary from "@/types/AntragSummary.ts";
import type Page from "@/types/Page.ts";

import { beforeEach, describe, expect, test, vi } from "vitest";

import { getAntragsSummaryList } from "@/api/fetch-antragSummary-list.ts";
import { useAntragSummaryList } from "@/composables/useAntragSummaryList";
import { STATUS_INDICATORS } from "@/constants.ts";
import { useAntragListFilterStore } from "@/stores/antragListFilter.ts";
import { useSnackbarStore } from "@/stores/snackbar.ts";
import {
  AntragListFilter,
  getEmptyAntragListFilter,
} from "@/types/AntragListFilter.ts";

vi.mock("@/api/fetch-antragSummary-list.ts");
vi.mock("@/stores/snackbar.ts");
vi.mock("@/stores/antragListFilter.ts");

describe("useAntragSummaryList", () => {
  let snackbarStoreMock: { showMessage: ReturnType<typeof vi.fn> };
  let filterStoreMock: {
    filters: AntragListFilter;
    setFilters: ReturnType<typeof vi.fn>;
    getFilters: AntragListFilter;
    $subscribe: ReturnType<typeof vi.fn>;
  };
  const filtersValue = getEmptyAntragListFilter();

  beforeEach(() => {
    snackbarStoreMock = {
      showMessage: vi.fn(),
    };
    filterStoreMock = {
      filters: filtersValue,
      setFilters: vi.fn(),
      getFilters: filtersValue,
      $subscribe: vi.fn(),
    };
    (useSnackbarStore as vi.Mock).mockReturnValue(snackbarStoreMock);
    (useAntragListFilterStore as vi.Mock).mockReturnValue(filterStoreMock);
  });

  test("testFetchItemsSuccessfully", async () => {
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

  test("testHandleErrorsWhenFetchingItems", async () => {
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

  test("testHandleGenericErrorsWhenFetchingItems", async () => {
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

  test("testFetchNewItemsWhenUpdateOptions", async () => {
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
      expect(getAntragsSummaryList).toHaveBeenCalledWith(1, 5, filtersValue);
      expect(items.value).toEqual(mockResponse.content);
    });
  });

  test("testUpdateOptionsCorrectly", () => {
    const { updateOptions, page, itemsPerPage } = useAntragSummaryList();

    updateOptions({ page: 3, itemsPerPage: 15 });

    expect(page.value).toBe(3);
    expect(itemsPerPage.value).toBe(15);
  });
});
