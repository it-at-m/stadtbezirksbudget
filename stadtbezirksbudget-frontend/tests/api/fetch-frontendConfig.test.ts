import type FrontendConfig from "@/types/FrontendConfig";

import { afterEach, describe, expect, test, vi } from "vitest";

import { getFrontendConfig } from "@/api/fetch-frontendConfig.ts";
import { BACKEND } from "@/constants.ts";

global.fetch = vi.fn();

const mockResponse: FrontendConfig = {
  zammadBaseUrl: "https://zammad.muenchen.de",
  eakteBaseUrl: "https://akte.muenchen.de",
};

describe("fetch-frontendConfig", () => {
  afterEach(() => {
    vi.clearAllMocks();
  });

  test("fetches with correct parameters", async () => {
    (fetch as ReturnType<typeof vi.fn>).mockResolvedValueOnce({
      ok: true,
      json: async () => mockResponse,
    });

    const result = await getFrontendConfig();

    expect(fetch).toHaveBeenCalledWith(
      `${BACKEND}/frontend-config`,
      expect.any(Object)
    );
    expect(result).toEqual(mockResponse);
  });

  test("handles network errors", async () => {
    const errorMessage = "Network Error";
    (fetch as ReturnType<typeof vi.fn>).mockRejectedValueOnce(
      new Error(errorMessage)
    );

    await expect(getFrontendConfig()).rejects.toThrow(
      "Es ist ein unbekannter Fehler aufgetreten."
    );
  });

  test("handles http errors", async () => {
    (fetch as ReturnType<typeof vi.fn>).mockResolvedValueOnce({
      ok: false,
      status: 404,
      statusText: "Not Found",
    });

    await expect(getFrontendConfig()).rejects.toThrow();
  });
});
