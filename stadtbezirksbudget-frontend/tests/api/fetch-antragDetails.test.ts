import type { AntragDetails } from "@/types/AntragDetails";

import { afterEach, describe, expect, test, vi } from "vitest";

import { getAntragDetails } from "@/api/fetch-AntragDetails.ts";
import { BACKEND } from "@/constants.ts";

global.fetch = vi.fn();

const mockResponse: AntragDetails = {
  projektTitel: "Test Projekt",
  eingangDatum: "2023-10-01T12:00:00Z",
  antragstellerName: "Max Mustermann",
  beantragtesBudget: 15000,
  rubrik: "Test Rubrik",
  status: "EINGEGANGEN",
  zammadNr: "00000021",
  aktenzeichen: "0000.0-00-0021",
  istGegendert: true,
  anmerkungen: "Test Anmerkungen",
};

describe("fetch-antragDetails", () => {
  afterEach(() => {
    vi.clearAllMocks();
  });

  test("fetches with correct parameters", async () => {
    (fetch as ReturnType<typeof vi.fn>).mockResolvedValueOnce({
      ok: true,
      json: async () => mockResponse,
    });

    const id = "80000000-0000-0000-0000-000000000013";
    const result = await getAntragDetails(id);

    expect(fetch).toHaveBeenCalledWith(
      `${BACKEND}/antrag/${id}`,
      expect.any(Object)
    );
    expect(result).toEqual(mockResponse);
  });

  test("handles network errors", async () => {
    const errorMessage = "Network Error";
    (fetch as ReturnType<typeof vi.fn>).mockRejectedValueOnce(
      new Error(errorMessage)
    );

    await expect(getAntragDetails()).rejects.toThrow();
  });

  test("handles http errors", async () => {
    (fetch as ReturnType<typeof vi.fn>).mockResolvedValueOnce({
      ok: false,
      status: 404,
      statusText: "Not Found",
    });

    await expect(getAntragDetails()).rejects.toThrow();
  });
});
