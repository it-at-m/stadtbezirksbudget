import { mount } from "@vue/test-utils";
import { beforeEach, describe, expect, test, vi } from "vitest";

import { DefaultLhmAvatarService } from "@/api/ad2image-avatar-client";
import Ad2ImageAvatar from "@/components/common/Ad2ImageAvatar.vue";

describe("Ad2ImageAvatar", () => {
  beforeEach(() => {
    vi.restoreAllMocks();
  });

  const mountWithVuetifyStub = (props: Record<string, unknown>) =>
    mount(Ad2ImageAvatar, {
      props,
      global: {
        stubs: {
          "v-avatar": { template: "<div><slot /></div>" },
        },
      },
    });

  test("testRendersImgWithProvidedPropsAndCallsAvatarHref", () => {
    const spy = vi
      .spyOn(DefaultLhmAvatarService, "avatarHref")
      .mockReturnValue("https://example/avatar?uid=user2");

    const wrapper = mountWithVuetifyStub({
      username: "user2",
      avatarMode: "modeX",
      avatarSize: "128",
    });

    const img = wrapper.find("img");
    expect(spy).toHaveBeenCalledWith("user2", "modeX", "128");
    expect(img.exists()).toBe(true);
    expect(img.attributes("src")).toBe("https://example/avatar?uid=user2");
    expect(img.attributes("alt")).toBe("Bild von user2");
  });

  test("testUsesDefaultPropsWhenOmitted", () => {
    const spy = vi
      .spyOn(DefaultLhmAvatarService, "avatarHref")
      .mockReturnValue("https://example/default");

    const wrapper = mountWithVuetifyStub({ username: "user1" });

    expect(spy).toHaveBeenCalledWith("user1", "fallbackGeneric", "64");
    const img = wrapper.find("img");
    expect(img.attributes("src")).toBe("https://example/default");
    expect(img.attributes("alt")).toBe("Bild von user1");
  });

  test("testHandlesEmptyUsername", () => {
    const spy = vi
      .spyOn(DefaultLhmAvatarService, "avatarHref")
      .mockReturnValue("https://example/empty");

    const wrapper = mountWithVuetifyStub({ username: "" });

    expect(spy).toHaveBeenCalledWith("", "fallbackGeneric", "64");
    const img = wrapper.find("img");
    expect(img.attributes("alt")).toBe("Bild von ");
  });
});
