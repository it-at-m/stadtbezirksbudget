import { describe, expect, test } from "vitest";

import { Ad2imageAvatarClient } from "@/api/ad2image-avatar-client";

describe("Ad2imageAvatarClient", () => {
  test("testBuildsCorrectUrl", () => {
    const base = "https://ad2image.example.com";
    const client = new Ad2imageAvatarClient(base);

    const href = client.avatarHref("test.test");

    expect(href).toContain("https://ad2image.example.com/avatar");
    expect(href).toContain("uid=test.test");
    expect(href).toContain("m=fallbackGeneric");
    expect(href).toContain("size=64");
  });

  test("testCustomModeAndSize", () => {
    const base = "https://ad2image.example.com/"; // trailing slash should be handled
    const client = new Ad2imageAvatarClient(base);

    const href = client.avatarHref("test.test", "letterAvatar", "128");

    expect(href).toContain("https://ad2image.example.com/avatar");
    expect(href).toContain("uid=test.test");
    expect(href).toContain("m=letterAvatar");
    expect(href).toContain("size=128");
  });
});
