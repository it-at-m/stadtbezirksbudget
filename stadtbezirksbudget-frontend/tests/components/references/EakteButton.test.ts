import { mount } from "@vue/test-utils";
import { beforeEach, describe, expect, test, vi } from "vitest";
import { computed, MaybeRefOrGetter, toValue } from "vue";

import EakteButton from "@/components/references/EakteButton.vue";
import { useReferenceLink } from "@/composables/useReferenceLink.ts";
import vuetify from "@/plugins/vuetify.ts";

vi.mock("@/composables/useReferenceLink.ts");
const mockUseReferenceLink = {
  getEakteLink: (adresse: MaybeRefOrGetter<string>) =>
    computed(() => `https://akte.muenchen.invalid?coo=${toValue(adresse)}`),
};

const eakteCooAdresse = "COO.1234.5678.9.0123456";
const createWrapper = () =>
  mount(EakteButton, {
    global: { plugins: [vuetify] },
    props: { eakteCooAdresse },
  });

describe("EakteButton", () => {
  beforeEach(() => {
    useReferenceLink.mockReturnValue(mockUseReferenceLink);
  });

  test("renders eakte button with correct label and href", () => {
    const wrapper = createWrapper();
    const button = wrapper.findComponent({ name: "v-btn-primary" });
    expect(button.exists()).toBe(true);
    expect(button.text()).toBe("E-Akte öffnen");
    expect(button.attributes("href")).toBe(
      "https://akte.muenchen.invalid?coo=COO.1234.5678.9.0123456"
    );
  });
});
