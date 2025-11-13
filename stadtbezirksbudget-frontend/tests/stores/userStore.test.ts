import { createPinia, setActivePinia } from "pinia";
import { beforeEach, describe, expect, it } from "vitest";

import { useUserStore } from "@/stores/user";
import User, { UserLocalDevelopment } from "@/types/User";

describe("user store", () => {
  beforeEach(() => {
    setActivePinia(createPinia());
  });

  it("testInitiallyNullUser", () => {
    const store = useUserStore();
    expect(store.getUser).toBeNull();
  });

  it("testSetUserStoresUser", () => {
    const store = useUserStore();
    const user = new User();
    user.username = "testuser";
    store.setUser(user);
    expect(store.getUser).not.toBeNull();
    expect(store.getUser?.username).toBe("testuser");
  });

  it("testClearUser", () => {
    const store = useUserStore();
    store.setUser(UserLocalDevelopment());
    expect(store.getUser).not.toBeNull();
    store.setUser(null);
    expect(store.getUser).toBeNull();
  });
});
