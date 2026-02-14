import { mount } from "@vue/test-utils";
import { beforeEach, describe, expect, test, vi } from "vitest";
import { computed, MaybeRefOrGetter, toValue } from "vue";

import EakteLink from "@/components/references/EakteLink.vue";
import { useReferenceLink } from "@/composables/useReferenceLink.ts";
import vuetify from "@/plugins/vuetify.ts";

vi.mock("@/composables/useReferenceLink.ts");
const mockUseReferenceLink = {
  getEakteLink: (adresse: MaybeRefOrGetter<string>) =>
    computed(() => `https://akte.muenchen.invalid?coo=${toValue(adresse)}`),
};

const eakteCooAdresse = "COO.1234.5678.9.0123456";
const aktenzeichen = "2025-00-00/12345";
const createWrapper = () =>
  mount(EakteLink, {
    global: { plugins: [vuetify] },
    props: { eakteCooAdresse, aktenzeichen },
  });

describe("EakteLink", () => {
  beforeEach(() => {
    useReferenceLink.mockReturnValue(mockUseReferenceLink);
  });

  test("renders a link with aktenzeichen and correct href", () => {
    const wrapper = createWrapper();
    const link = wrapper.find("a");
    expect(link.exists()).toBe(true);
    expect(link.text()).toBe(aktenzeichen);
    expect(link.attributes("href")).toBe(
      "https://akte.muenchen.invalid?coo=COO.1234.5678.9.0123456"
    );
  });
});
