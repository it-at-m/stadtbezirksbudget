import { afterEach, describe, expect, test, vi } from "vitest";

import { updateAntragReference } from "@/api/update-antragReference.ts";
import { BACKEND } from "@/constants.ts";

global.fetch = vi.fn();

describe("update-antragReference", () => {
  afterEach(() => {
    vi.clearAllMocks();
  });

  test("resolves on successful response", async () => {
    (fetch as ReturnType<typeof vi.fn>).mockResolvedValueOnce({
      ok: true,
      status: 200,
      json: async () => ({}),
    });

    await expect(
      updateAntragReference("123", {
        eakteCooAdresse: "COO.6804.7915.3.3210800",
      })
    ).resolves.toBeUndefined();

    expect(fetch).toHaveBeenCalledWith(
      `${BACKEND}/antrag/123/reference`,
      expect.objectContaining({
        method: "PATCH",
        body: JSON.stringify({
          eakteCooAdresse: "COO.6804.7915.3.3210800",
        }),
      })
    );
  });

  test("throws on network error", async () => {
    (fetch as ReturnType<typeof vi.fn>).mockRejectedValueOnce(
      new Error("Network Error")
    );

    await expect(
      updateAntragReference("456", {
        eakteCooAdresse: "COO.6804.7915.3.3210800",
      })
    ).rejects.toThrow("Es ist ein unbekannter Fehler aufgetreten.");
  });

  test("throws on 404 error", async () => {
    (fetch as ReturnType<typeof vi.fn>).mockResolvedValueOnce({
      ok: false,
      status: 404,
      statusText: "Not Found",
    });

    await expect(
      updateAntragReference("789", {
        eakteCooAdresse: "COO.6804.7915.3.3210800",
      })
    ).rejects.toThrow("Es ist ein unbekannter Fehler aufgetreten.");
  });
});
