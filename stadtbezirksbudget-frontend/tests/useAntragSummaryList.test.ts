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
  let snackbarStoreMock;

  beforeEach(() => {
    snackbarStoreMock = {
      showMessage: vi.fn(),
    };
    (useSnackbarStore as vi.Mock).mockReturnValue(snackbarStoreMock);
  });

  test("testFetchItemsSuccessfully", async () => {
    const mockResponse: Page<AntragSummary> = {
      content: [{ id: 1, name: "Antrag 1" }],
      page: { totalElements: 1 },
    };

    (getAntragsSummaryList as vi.Mock).mockResolvedValue(mockResponse);
    const { items, fetchItems, loading } = useAntragSummaryList();

    await fetchItems();

    expect(items.value).toEqual(mockResponse.content);
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
      content: [{ id: 2, name: "Antrag 2" }],
      page: { totalElements: 1 },
    };

    (getAntragsSummaryList as vi.Mock).mockResolvedValue(mockResponse);
    const { updateOptions, items } = useAntragSummaryList();

    updateOptions({ page: 2, itemsPerPage: 5 });

    await new Promise((resolve) => setTimeout(resolve, 0));

    expect(items.value).toEqual(mockResponse.content);
  });

  test("testUpdateOptionsCorrectly", () => {
    const { updateOptions, page, itemsPerPage } = useAntragSummaryList();

    updateOptions({ page: 3, itemsPerPage: 15 });

    expect(page.value).toBe(3);
    expect(itemsPerPage.value).toBe(15);
  });
});
