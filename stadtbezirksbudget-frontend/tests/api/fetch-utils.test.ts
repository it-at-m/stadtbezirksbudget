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

  test("testGetConfigHasDefaultHeaders", () => {
    const cfg = getConfig();
    expect(cfg).toBeDefined();
    expect(cfg.mode).toBe("cors");
    expect((cfg.headers as Headers).get("Content-Type")).toBe(
      "application/json"
    );
  });

  test("testPostConfigIncludesBodyAndHeaders", () => {
    const body = { a: 1 };
    const cfg = postConfig(body);
    expect(cfg.method).toBe("POST");
    expect(cfg.body).toBe(JSON.stringify(body));
    expect((cfg.headers as Headers).get("Content-Type")).toBe(
      "application/json"
    );
  });

  test("testPutConfigAppendsIfMatchWhenVersionPresent", () => {
    const body = { id: 1, version: "v1" };
    // eslint-disable-next-line
    const cfg = putConfig(body as any);
    expect(cfg.method).toBe("PUT");
    expect(cfg.body).toBe(JSON.stringify(body));
    expect((cfg.headers as Headers).get("If-Match")).toBe("v1");
  });

  test("testPatchConfigAppendsIfMatchWhenVersionNotUndefined", () => {
    const body = { id: 1, version: 0 };
    // eslint-disable-next-line
    const cfg = patchConfig(body as any);
    expect(cfg.method).toBe("PATCH");
    expect(cfg.body).toBe(JSON.stringify(body));
    expect((cfg.headers as Headers).get("If-Match")).toBe("0");
  });

  test("testDeleteConfigHasDeleteMethod", () => {
    const cfg = deleteConfig();
    expect(cfg.method).toBe("DELETE");
    expect((cfg.headers as Headers).get("Content-Type")).toBe(
      "application/json"
    );
  });

  test("testGetHeadersIncludeXSRFTokenWhenCookiePresent", () => {
    vi.stubGlobal("document", { cookie: "XSRF-TOKEN=abc123; other=1" });
    const cfg = getConfig();
    expect((cfg.headers as Headers).get("X-XSRF-TOKEN")).toBe("abc123");
  });

  test("testGetHeadersOmitXSRFTokenWhenCookieAbsent", () => {
    const cfg = getConfig();
    expect((cfg.headers as Headers).get("X-XSRF-TOKEN")).toBeNull();
  });

  test("testPutConfigOmitsIfMatchWhenNoVersion", () => {
    const body = { id: 1 };
    // eslint-disable-next-line
    const cfg = putConfig(body as any);
    expect(cfg.method).toBe("PUT");
    expect(cfg.body).toBe(JSON.stringify(body));
    expect((cfg.headers as Headers).get("If-Match")).toBeNull();
  });

  test("testPatchConfigOmitsIfMatchWhenVersionUndefined", () => {
    const body = { id: 1, version: undefined };
    // eslint-disable-next-line
    const cfg = patchConfig(body as any);
    expect(cfg.method).toBe("PATCH");
    expect(cfg.body).toBe(JSON.stringify(body));
    expect((cfg.headers as Headers).get("If-Match")).toBeNull();
  });

  test("testPostConfigHandlesNullOrUndefinedBody", () => {
    // eslint-disable-next-line
    const cfgNull = postConfig(null as any);
    expect(cfgNull.method).toBe("POST");
    expect(cfgNull.body).toBeUndefined();
    expect((cfgNull.headers as Headers).get("Content-Type")).toBe(
      "application/json"
    );
    // eslint-disable-next-line
    const cfgUndef = postConfig(undefined as any);
    expect(cfgUndef.method).toBe("POST");
    expect(cfgUndef.body).toBeUndefined();
    expect((cfgUndef.headers as Headers).get("Content-Type")).toBe(
      "application/json"
    );
  });

  test("testDefaultResponseHandlerDoesNothingOnOk", () => {
    const resp = { ok: true } as Response;
    expect(() => defaultResponseHandler(resp)).not.toThrow();
  });

  test("testDefaultResponseHandlerThrowsApiErrorOn403", () => {
    const resp = { ok: false, status: 403 } as Response;
    expect(() => defaultResponseHandler(resp)).toThrow(ApiError);
    try {
      defaultResponseHandler(resp);
    } catch (e) {
      if (e instanceof ApiError) {
        expect(e.level).toBe(STATUS_INDICATORS.ERROR);
        expect(e.message as string).toContain("nicht die nötigen Rechte");
      }
    }
  });

  test("testDefaultResponseHandlerReloadsOnOpaqueRedirect", () => {
    const reloadMock = vi.fn();
    vi.stubGlobal("location", { reload: reloadMock });

    const resp = { ok: false, type: "opaqueredirect" } as unknown as Response;
    expect(() => defaultResponseHandler(resp)).toThrow(ApiError);
    expect(reloadMock).toHaveBeenCalled();
  });

  test("testDefaultResponseHandlerThrowsWarningOnOtherErrors", () => {
    const resp = { ok: false, status: 500 } as Response;
    expect(() => defaultResponseHandler(resp, "FehlerX")).toThrow(ApiError);
    try {
      defaultResponseHandler(resp, "FehlerX");
    } catch (e) {
      if (e instanceof ApiError) {
        expect(e.level).toBe(STATUS_INDICATORS.WARNING);
        expect(e.message).toBe("FehlerX");
      }
    }
  });

  test("testDefaultCatchHandlerThrowsApiErrorWithMessage", async () => {
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
