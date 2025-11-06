import { afterEach, describe, expect, test, vi } from "vitest";

import { getAntragsSummaryList } from "@/api/fetch-antragSummary-list.ts";
import { BACKEND } from "@/constants.ts";

global.fetch = vi.fn();

describe("getAntragsSummaryList", () => {
  afterEach(() => {
    vi.clearAllMocks();
  });

  test("testFetchSuccessful", async () => {
    const mockResponse = {
      content: [
        /*...*/
      ],
      page: { size: 5, number: 1, totalElements: 1, totalPages: 1 },
    };

    (fetch as ReturnType<typeof vi.fn>).mockResolvedValueOnce({
      ok: true,
      json: async () => mockResponse,
    });

    const result = await getAntragsSummaryList(1, 5);

    expect(fetch).toHaveBeenCalledWith(
      `${BACKEND}/antrag?page=1&size=5`,
      expect.any(Object)
    );
    expect(result).toEqual(mockResponse);
  });

  test("testHandleErrorsWhenFetching", async () => {
    const errorMessage = "Network Error";
    (fetch as ReturnType<typeof vi.fn>).mockRejectedValueOnce(
      new Error(errorMessage)
    );

    await expect(getAntragsSummaryList(1, 5)).rejects.toThrow(
      "Fehler beim Laden der Antragsliste."
    );
  });

  test("testHandleHttpErrorResponse", async () => {
    (fetch as ReturnType<typeof vi.fn>).mockResolvedValueOnce({
      ok: false,
      status: 404,
      statusText: "Not Found",
    });

    await expect(getAntragsSummaryList(1, 5)).rejects.toThrow();
  });
});
