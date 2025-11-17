import { describe, expect, test } from "vitest";

import { Ad2imageAvatarClient } from "@/api/ad2image-avatar-client";

describe("Ad2imageAvatarClient", () => {
  test("testBuildsCorrectUrl", () => {
    const base = "https://ad2image.example.com";
    const client = new Ad2imageAvatarClient(base);

    const href = client.avatarHref("test.test");

    expect(href).toBe(
      "https://ad2image.example.com/avatar?uid=test.test&m=fallbackGeneric&size=64"
    );
  });

  test("testCustomModeAndSize", () => {
    const base = "https://ad2image.example.com/"; // trailing slash should be handled
    const client = new Ad2imageAvatarClient(base);

    const href = client.avatarHref("test.test", "letterAvatar", "128");

    expect(href).toBe(
      "https://ad2image.example.com/avatar?uid=test.test&m=letterAvatar&size=128"
    );
  });
});
