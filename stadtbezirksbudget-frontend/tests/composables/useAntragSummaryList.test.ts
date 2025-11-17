import type AntragSummary from "@/types/AntragSummary.ts";
import type Page from "@/types/Page.ts";

import { beforeEach, describe, expect, test, vi } from "vitest";

import { getAntragsSummaryList } from "@/api/fetch-antragSummary-list.ts";
import { useAntragSummaryList } from "@/composables/useAntragSummaryList";
import { STATUS_INDICATORS } from "@/constants.ts";
import { useSnackbarStore } from "@/stores/snackbar.ts";

vi.mock("@/api/fetch-antragSummary-list.ts");
vi.mock("@/stores/snackbar.ts");

describe("useAntragSummaryList", () => {
  let snackbarStoreMock: { showMessage: ReturnType<typeof vi.fn> };

  beforeEach(() => {
    snackbarStoreMock = {
      showMessage: vi.fn(),
    };
    (useSnackbarStore as vi.Mock).mockReturnValue(snackbarStoreMock);
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

    await fetchItems();

    expect(items.value).toEqual(mockResponse.content);
    expect(totalItems.value).toBe(mockResponse.page.totalElements);
    expect(loading.value).toBe(false);
  });

  test("testHandleErrorsWhenFetchingItems", async () => {
    const errorMessage = "API Error";
    (getAntragsSummaryList as vi.Mock).mockRejectedValue(
      new Error(errorMessage)
    );

    const { fetchItems, loading } = useAntragSummaryList();
    await fetchItems();

    expect(snackbarStoreMock.showMessage).toHaveBeenCalledWith({
      message: errorMessage,
      level: STATUS_INDICATORS.WARNING,
    });
    expect(loading.value).toBe(false);
  });

  test("testHandleGenericErrorsWhenFetchingItems", async () => {
    (getAntragsSummaryList as vi.Mock).mockRejectedValue(new Error());

    const { fetchItems } = useAntragSummaryList();
    await fetchItems();

    expect(snackbarStoreMock.showMessage).toHaveBeenCalledWith({
      message: "Fehler beim Laden der Anträge",
      level: STATUS_INDICATORS.WARNING,
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

    // Wait for the fetchItems promise to resolve
    await new Promise((resolve) => setTimeout(resolve, 0));

    expect(getAntragsSummaryList).toHaveBeenCalledWith(1, 5);
    expect(items.value).toEqual(mockResponse.content);
  });

  test("testUpdateOptionsCorrectly", () => {
    const { updateOptions, page, itemsPerPage } = useAntragSummaryList();

    updateOptions({ page: 3, itemsPerPage: 15 });

    expect(page.value).toBe(3);
    expect(itemsPerPage.value).toBe(15);
  });
});
