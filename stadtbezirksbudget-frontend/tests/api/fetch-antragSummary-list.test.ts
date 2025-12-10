import type { AntragListFilter } from "@/types/AntragListFilter";

import { afterEach, describe, expect, test, vi } from "vitest";

import { getAntragsSummaryList } from "@/api/fetch-antragSummary-list.ts";
import { BACKEND } from "@/constants.ts";
import { defaultAntragListFilter } from "@/types/AntragListFilter";
import { antragListFilterToDTO } from "@/types/AntragListFilterDTO.ts";
import {
  AntragListSort,
  antragListSortToSortDto,
  createEmptyListSort,
} from "@/types/AntragListSort";
import { objectToSearchParams } from "@/util/converter";

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
const testFiltersString = objectToSearchParams(
  antragListFilterToDTO(testFilters)
).toString();

const testSorting: AntragListSort = {
  status: { sortBy: "status", sortDirection: "asc", title: "Status" },
};

const testSortingString = objectToSearchParams(
  antragListSortToSortDto(testSorting)
).toString();

const mockResponse = {
  content: [],
  page: { size: 5, number: 1, totalElements: 1, totalPages: 1 },
};

describe("fetch-antragSummary-list", () => {
  afterEach(() => {
    vi.clearAllMocks();
  });

  test("fetches with correct parameters without filters", async () => {
    (fetch as ReturnType<typeof vi.fn>).mockResolvedValueOnce({
      ok: true,
      json: async () => mockResponse,
    });

    const result = await getAntragsSummaryList(1, 5, {}, createEmptyListSort());

    expect(fetch).toHaveBeenCalledWith(
      `${BACKEND}/antrag?page=1&size=5`,
      expect.any(Object)
    );
    expect(result).toEqual(mockResponse);
  });

  test("fetches with correct parameters when filters are applied", async () => {
    (fetch as ReturnType<typeof vi.fn>).mockResolvedValueOnce({
      ok: true,
      json: async () => mockResponse,
    });

    await getAntragsSummaryList(1, 5, testFilters, createEmptyListSort());

    expect(fetch).toHaveBeenCalledWith(
      `${BACKEND}/antrag?page=1&size=5&${testFiltersString}`,
      expect.any(Object)
    );
  });

  test("fetches with correct parameters when sorting is applied", async () => {
    (fetch as ReturnType<typeof vi.fn>).mockResolvedValueOnce({
      ok: true,
      json: async () => mockResponse,
    });

    await getAntragsSummaryList(1, 5, {}, testSorting);

    expect(fetch).toHaveBeenCalledWith(
      `${BACKEND}/antrag?page=1&size=5&${testSortingString}`,
      expect.any(Object)
    );
  });

  test("fetches with correct parameters when both filters and sorting are applied", async () => {
    (fetch as ReturnType<typeof vi.fn>).mockResolvedValueOnce({
      ok: true,
      json: async () => mockResponse,
    });

    await getAntragsSummaryList(1, 5, testFilters, testSorting);

    expect(fetch).toHaveBeenCalledWith(
      `${BACKEND}/antrag?page=1&size=5&${testFiltersString}&${testSortingString}`,
      expect.any(Object)
    );
  });

  test("handles network errors", async () => {
    const errorMessage = "Network Error";
    (fetch as ReturnType<typeof vi.fn>).mockRejectedValueOnce(
      new Error(errorMessage)
    );

    await expect(
      getAntragsSummaryList(
        1,
        5,
        defaultAntragListFilter(),
        createEmptyListSort()
      )
    ).rejects.toThrow("Fehler beim Laden der Antragsliste.");
  });

  test("handles http errors", async () => {
    (fetch as ReturnType<typeof vi.fn>).mockResolvedValueOnce({
      ok: false,
      status: 404,
      statusText: "Not Found",
    });

    await expect(
      getAntragsSummaryList(
        1,
        5,
        defaultAntragListFilter(),
        createEmptyListSort()
      )
    ).rejects.toThrow();
  });
});
