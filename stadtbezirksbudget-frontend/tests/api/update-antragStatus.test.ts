import { afterEach, describe, expect, test, vi } from "vitest";

import { updateAntragStatus } from "@/api/update-antragStatus.ts";
import { BACKEND } from "@/constants.ts";

global.fetch = vi.fn();

describe("updateAntragStatus", () => {
  afterEach(() => {
    vi.clearAllMocks();
  });

  test("resolves on successful 2xx response", async () => {
    (fetch as ReturnType<typeof vi.fn>).mockResolvedValueOnce({
      ok: true,
      status: 200,
      json: async () => ({}),
    });

    await expect(
      updateAntragStatus("123", "EINGEGANGEN")
    ).resolves.toBeUndefined();

    expect(fetch).toHaveBeenCalledWith(
      `${BACKEND}/antrag/123/status`,
      expect.objectContaining({
        method: "PATCH",
        body: JSON.stringify({ status: "EINGEGANGEN" }),
      })
    );
  });

  test("rejects with ApiError message on network failure", async () => {
    (fetch as ReturnType<typeof vi.fn>).mockRejectedValueOnce(
      new Error("Network Error")
    );

    await expect(updateAntragStatus("456", "ABGELEHNT")).rejects.toThrow(
      "Fehler beim Aktualisieren des Antragsstatus"
    );
  });

  test("rejects with ApiError message on non-ok HTTP response", async () => {
    (fetch as ReturnType<typeof vi.fn>).mockResolvedValueOnce({
      ok: false,
      status: 404,
      statusText: "Not Found",
    });

    await expect(updateAntragStatus("789", "ABGELEHNT")).rejects.toThrow(
      "Fehler beim Aktualisieren des Antragsstatus"
    );
  });
});
