import { describe, expect, test } from "vitest";

import { Ad2imageAvatarClient } from "@/api/ad2image-avatar-client";

describe("ad2image-avatar-client", () => {
  test("builds correct URL", () => {
    const base = "https://ad2image.example.com";
    const client = new Ad2imageAvatarClient(base);

    const href = client.avatarHref("test.test");

    expect(href).toBe(
      "https://ad2image.example.com/avatar?uid=test.test&m=fallbackGeneric&size=64"
    );
  });

  test("builds correct URL with custom mode and size", () => {
    const base = "https://ad2image.example.com/"; // trailing slash should be handled
    const client = new Ad2imageAvatarClient(base);

    const href = client.avatarHref("test.test", "letterAvatar", "128");

    expect(href).toBe(
      "https://ad2image.example.com/avatar?uid=test.test&m=letterAvatar&size=128"
    );
  });

  test("builds correct URL with special characters", () => {
    const client = new Ad2imageAvatarClient("https://ad2image.example.com");

    const href = client.avatarHref("test user@domain");

    const url = new URL(href);
    expect(url.searchParams.get("uid")).toBe("test user@domain");
  });
});
