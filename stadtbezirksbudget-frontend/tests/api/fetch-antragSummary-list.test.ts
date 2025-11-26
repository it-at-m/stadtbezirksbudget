import { afterEach, describe, expect, test, vi } from "vitest";

import { getAntragsSummaryList } from "@/api/fetch-antragSummary-list.ts";
import { BACKEND } from "@/constants.ts";
import {
  AntragListFilter,
  emptyAntragListFilter,
} from "@/types/AntragListFilter";
import { antragListFilterToDTO, appendSearchParams } from "@/util/converter";

global.fetch = vi.fn();

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
const testFiltersString = appendSearchParams(
  antragListFilterToDTO(testFilters)
).toString();

const mockResponse = {
  content: [],
  page: { size: 5, number: 1, totalElements: 1, totalPages: 1 },
};

describe("getAntragsSummaryList", () => {
  afterEach(() => {
    vi.clearAllMocks();
  });

  test("testFetchSuccessful", async () => {
    (fetch as ReturnType<typeof vi.fn>).mockResolvedValueOnce({
      ok: true,
      json: async () => mockResponse,
    });

    const result = await getAntragsSummaryList(1, 5, emptyAntragListFilter());

    expect(fetch).toHaveBeenCalledWith(
      `${BACKEND}/antrag?page=1&size=5`,
      expect.any(Object)
    );
    expect(result).toEqual(mockResponse);
  });

  test("testFetchWithFilters", async () => {
    (fetch as ReturnType<typeof vi.fn>).mockResolvedValueOnce({
      ok: true,
      json: async () => mockResponse,
    });

    await getAntragsSummaryList(1, 5, testFilters);

    expect(fetch).toHaveBeenCalledWith(
      `${BACKEND}/antrag?page=1&size=5&${testFiltersString}`,
      expect.any(Object)
    );
  });

  test("testHandleErrorsWhenFetching", async () => {
    const errorMessage = "Network Error";
    (fetch as ReturnType<typeof vi.fn>).mockRejectedValueOnce(
      new Error(errorMessage)
    );

    await expect(
      getAntragsSummaryList(1, 5, emptyAntragListFilter())
    ).rejects.toThrow("Fehler beim Laden der Antragsliste.");
  });

  test("testHandleHttpErrorResponse", async () => {
    (fetch as ReturnType<typeof vi.fn>).mockResolvedValueOnce({
      ok: false,
      status: 404,
      statusText: "Not Found",
    });

    await expect(
      getAntragsSummaryList(1, 5, emptyAntragListFilter())
    ).rejects.toThrow();
  });
});
