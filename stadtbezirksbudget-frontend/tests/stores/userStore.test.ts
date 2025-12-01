import { createPinia, setActivePinia } from "pinia";
import { beforeEach, describe, expect, test } from "vitest";

import { useUserStore } from "@/stores/useUserStore.ts";
import User, { UserLocalDevelopment } from "@/types/User";

describe("user store", () => {
  beforeEach(() => {
    setActivePinia(createPinia());
  });

  test("testInitiallyNullUser", () => {
    const store = useUserStore();
    expect(store.getUser).toBeNull();
  });

  test("testSetUserStoresUser", () => {
    const store = useUserStore();
    const user = new User();
    user.username = "testuser";
    store.setUser(user);
    expect(store.getUser).not.toBeNull();
    expect(store.getUser?.username).toBe("testuser");
  });

  test("testClearUser", () => {
    const store = useUserStore();
    store.setUser(UserLocalDevelopment());
    expect(store.getUser).not.toBeNull();
    store.setUser(null);
    expect(store.getUser).toBeNull();
  });
});
