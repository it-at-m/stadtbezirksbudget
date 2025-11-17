import { afterEach, describe, expect, test, vi } from "vitest";

import * as fetchUtils from "@/api/fetch-utils";
import { getUser } from "@/api/user-client";
import User from "@/types/User";

global.fetch = vi.fn();

vi.spyOn(fetchUtils, "getConfig").mockImplementation(() => {
  return {
    headers: new Headers({ "Content-Type": "application/json" }),
  } as RequestInit;
});

describe("getUser", () => {
  afterEach(() => {
    vi.clearAllMocks();
    vi.restoreAllMocks();
  });

  test("testParsesCorrectResponse", async () => {
    const mockJson = {
      sub: "123",
      displayName: "Test",
      surname: "Test",
      telephoneNumber: "01234",
      email: "test@example.com",
      username: "test",
      givenname: "Test",
      department: "test",
      lhmObjectID: "TEST-1",
      preferred_username: "test",
      memberof: ["testm1"],
      user_roles: ["testu1"],
      authorities: ["testa1"],
    };

    (fetch as ReturnType<typeof vi.fn>).mockResolvedValueOnce({
      ok: true,
      json: async () => mockJson,
    });

    const user = await getUser();

    expect(user).toBeInstanceOf(User);
    expect(user.sub).toBe("123");
    expect(user.displayName).toBe("Test");
    expect(user.username).toBe("test");
    expect(user.user_roles).toEqual(["testu1"]);
  });

  test("testParsesEmptyResponse", async () => {
    const mockJson = {
      sub: undefined,
      displayName: undefined,
      surname: undefined,
      telephoneNumber: undefined,
      email: undefined,
      username: undefined,
      givenname: undefined,
      department: undefined,
      lhmObjectID: undefined,
      preferred_username: undefined,
      memberof: undefined,
      user_roles: undefined,
      authorities: undefined,
    };

    (fetch as ReturnType<typeof vi.fn>).mockResolvedValueOnce({
      ok: true,
      json: async () => mockJson,
    });

    const user = await getUser();

    expect(user).toBeInstanceOf(User);
    expect(user.sub).toBe("");
    expect(user.displayName).toBe("");
    expect(user.username).toBe("");
    expect(user.user_roles).toEqual([]);
    expect(user.memberof).toEqual([]);
  });

  test("testThrowsWhenFetchRejected", async () => {
    (fetch as ReturnType<typeof vi.fn>).mockRejectedValueOnce(
      new Error("Network")
    );

    await expect(getUser()).rejects.toThrow();
  });

  test("testThrowsOnWrongResponse", async () => {
    (fetch as ReturnType<typeof vi.fn>).mockResolvedValueOnce({
      ok: false,
      status: 401,
      statusText: "Unauthorized",
    });

    await expect(getUser()).rejects.toThrow();
  });
});
