import { afterEach, beforeEach, describe, expect, test, vi } from "vitest";

import { ApiError } from "@/api/ApiError";
import {
  defaultCatchHandler,
  defaultResponseHandler,
  deleteConfig,
  getConfig,
  patchConfig,
  postConfig,
  putConfig,
} from "@/api/fetch-utils";
import { STATUS_INDICATORS } from "@/constants";

describe("fetch-utils", () => {
  beforeEach(() => {
    vi.stubGlobal("document", { cookie: "" });
  });

  afterEach(() => {
    vi.restoreAllMocks();
  });

  test("getConfig has default headers", () => {
    const cfg = getConfig();
    expect(cfg).toBeDefined();
    expect(cfg.mode).toBe("cors");
    expect((cfg.headers as Headers).get("Content-Type")).toBe(
      "application/json"
    );
  });

  test("getHeaders omit XSRF token when cookie absent", () => {
    const cfg = getConfig();
    expect((cfg.headers as Headers).get("X-XSRF-TOKEN")).toBeNull();
  });

  test("getHeaders include XSRF token when cookie present", () => {
    vi.stubGlobal("document", { cookie: "XSRF-TOKEN=abc123; other=1" });
    const cfg = getConfig();
    expect((cfg.headers as Headers).get("X-XSRF-TOKEN")).toBe("abc123");
  });

  test("postConfig includes body and headers", () => {
    const body = { a: 1 };
    const cfg = postConfig(body);
    expect(cfg.method).toBe("POST");
    expect(cfg.body).toBe(JSON.stringify(body));
    expect((cfg.headers as Headers).get("Content-Type")).toBe(
      "application/json"
    );
  });

  test("postConfig handles null or undefined body", () => {
    const cfgNull = postConfig(null);
    expect(cfgNull.method).toBe("POST");
    expect(cfgNull.body).toBeUndefined();
    expect((cfgNull.headers as Headers).get("Content-Type")).toBe(
      "application/json"
    );
    const cfgUndef = postConfig(undefined);
    expect(cfgUndef.method).toBe("POST");
    expect(cfgUndef.body).toBeUndefined();
    expect((cfgUndef.headers as Headers).get("Content-Type")).toBe(
      "application/json"
    );
  });

  test("putConfig omits If-Match when no version", () => {
    const body = { id: 1 };
    const cfg = putConfig(body);
    expect(cfg.method).toBe("PUT");
    expect(cfg.body).toBe(JSON.stringify(body));
    expect((cfg.headers as Headers).get("If-Match")).toBeNull();
  });

  test("putConfig appends If-Match when version present", () => {
    const body = { id: 1, version: "v1" };
    const cfg = putConfig(body);
    expect(cfg.method).toBe("PUT");
    expect(cfg.body).toBe(JSON.stringify(body));
    expect((cfg.headers as Headers).get("If-Match")).toBe("v1");
  });

  test("patchConfig appends If-Match when version present", () => {
    const body = { id: 1, version: 0 };
    const cfg = patchConfig(body);
    expect(cfg.method).toBe("PATCH");
    expect(cfg.body).toBe(JSON.stringify(body));
    expect((cfg.headers as Headers).get("If-Match")).toBe("0");
  });

  test("patchConfig omits If-Match when version undefined", () => {
    const body = { id: 1, version: undefined };
    const cfg = patchConfig(body);
    expect(cfg.method).toBe("PATCH");
    expect(cfg.body).toBe(JSON.stringify(body));
    expect((cfg.headers as Headers).get("If-Match")).toBeNull();
  });

  test("deleteConfig has DELETE method", () => {
    const cfg = deleteConfig();
    expect(cfg.method).toBe("DELETE");
    expect((cfg.headers as Headers).get("Content-Type")).toBe(
      "application/json"
    );
  });

  test("defaultResponseHandler does nothing on ok response", () => {
    const resp = { ok: true } as Response;
    expect(() => defaultResponseHandler(resp)).not.toThrow();
  });

  test("defaultResponseHandler throws ApiError on 403", () => {
    const resp = { ok: false, status: 403 } as Response;
    try {
      defaultResponseHandler(resp);
      expect.fail("Expected ApiError to be thrown");
    } catch (e) {
      if (e instanceof ApiError) {
        expect(e.level).toBe(STATUS_INDICATORS.ERROR);
        expect(e.message as string).toContain("nicht die nötigen Rechte");
      }
    }
  });

  test("defaultResponseHandler reloads on opaque redirect", () => {
    const reloadMock = vi.fn();
    vi.stubGlobal("location", { reload: reloadMock });

    const resp = { ok: false, type: "opaqueredirect" } as unknown as Response;
    try {
      defaultResponseHandler(resp);
      expect.fail("Expected ApiError to be thrown");
    } catch (e) {
      if (e instanceof ApiError) {
        expect(e.level).toBe(STATUS_INDICATORS.WARNING);
        expect(e.message as string).toBe(
          "Es ist ein unbekannter Fehler aufgetreten."
        );
      }
    }
    expect(reloadMock).toHaveBeenCalled();
  });

  test("defaultResponseHandler throws ApiError on other errors", () => {
    const resp = { ok: false, status: 500 } as Response;
    try {
      defaultResponseHandler(resp, "FehlerX");
      expect.fail("Expected ApiError to be thrown");
    } catch (e) {
      if (e instanceof ApiError) {
        expect(e.level).toBe(STATUS_INDICATORS.WARNING);
        expect(e.message).toBe("FehlerX");
      }
    }
  });

  test("defaultCatchHandler throws ApiError with message", async () => {
    await expect(async () =>
      defaultCatchHandler(new Error("boom"), "CatchMsg")
    ).rejects.toThrow(ApiError);
    try {
      await defaultCatchHandler(new Error("boom"), "CatchMsg");
    } catch (e) {
      if (e instanceof ApiError) {
        expect(e.level).toBe(STATUS_INDICATORS.WARNING);
        expect(e.message).toBe("CatchMsg");
      }
    }
  });
});
