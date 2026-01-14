import type { AntragListFilterOptions } from "@/types/AntragListFilter";

import { afterEach, describe, expect, test, vi } from "vitest";

import { getAntragListFilterOptions } from "@/api/fetch-antragListFilterOptions.ts";
import { BACKEND } from "@/constants.ts";

global.fetch = vi.fn();

const mockResponse: AntragListFilterOptions = {
  antragstellerNamen: ["TEST_NAME_1", "TEST_NAME_2"],
  projektTitel: ["TEST_TITLE_1", "TEST_TITLE_2", "TEST_TITLE_3"],
};

describe("fetch-antragListFilterOptions", () => {
  afterEach(() => {
    vi.clearAllMocks();
  });

  test("fetches with correct parameters", async () => {
    (fetch as ReturnType<typeof vi.fn>).mockResolvedValueOnce({
      ok: true,
      json: async () => mockResponse,
    });

    const result = await getAntragListFilterOptions();

    expect(fetch).toHaveBeenCalledWith(
      `${BACKEND}/antrag/filterOptions`,
      expect.any(Object)
    );
    expect(result).toEqual(mockResponse);
  });

  test("handles network errors", async () => {
    const errorMessage = "Network Error";
    (fetch as ReturnType<typeof vi.fn>).mockRejectedValueOnce(
      new Error(errorMessage)
    );

    await expect(getAntragListFilterOptions()).rejects.toThrow(
      "Es ist ein unbekannter Fehler aufgetreten."
    );
  });

  test("handles http errors", async () => {
    (fetch as ReturnType<typeof vi.fn>).mockResolvedValueOnce({
      ok: false,
      status: 404,
      statusText: "Not Found",
    });

    await expect(getAntragListFilterOptions()).rejects.toThrow();
  });
});
