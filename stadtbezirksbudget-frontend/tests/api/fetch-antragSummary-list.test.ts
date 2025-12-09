import { afterEach, describe, expect, test, vi } from "vitest";

import { getAntragsSummaryList } from "@/api/fetch-antragSummary-list.ts";
import { BACKEND } from "@/constants.ts";
import { defaultAntragListFilter } from "@/types/AntragListFilter";

global.fetch = vi.fn();

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

    const result = await getAntragsSummaryList(1, 5, {});

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

    await getAntragsSummaryList(1, 5, { filter: "value" });

    expect(fetch).toHaveBeenCalledWith(
      `${BACKEND}/antrag?page=1&size=5&filter=value`,
      expect.any(Object)
    );
  });

  test("handles network errors", async () => {
    const errorMessage = "Network Error";
    (fetch as ReturnType<typeof vi.fn>).mockRejectedValueOnce(
      new Error(errorMessage)
    );

    await expect(
      getAntragsSummaryList(1, 5, defaultAntragListFilter())
    ).rejects.toThrow("Fehler beim Laden der Antragsliste.");
  });

  test("handles http errors", async () => {
    (fetch as ReturnType<typeof vi.fn>).mockResolvedValueOnce({
      ok: false,
      status: 404,
      statusText: "Not Found",
    });

    await expect(
      getAntragsSummaryList(1, 5, defaultAntragListFilter())
    ).rejects.toThrow();
  });
});
