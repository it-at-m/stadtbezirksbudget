import { mount } from "@vue/test-utils";
import { beforeEach, describe, expect, test, vi } from "vitest";
import { computed, MaybeRefOrGetter, toValue } from "vue";

import ZammadButton from "@/components/references/ZammadButton.vue";
import { useReferenceLink } from "@/composables/useReferenceLink.ts";
import vuetify from "@/plugins/vuetify.ts";

vi.mock("@/composables/useReferenceLink.ts");
const mockUseReferenceLink = {
  getZammadLink: (nr: MaybeRefOrGetter<string>) =>
    computed(() => `https://zammad.muenchen.invalid?nr=${toValue(nr)}`),
};

const zammadNr = "00000018";
const createWrapper = () =>
  mount(ZammadButton, {
    global: { plugins: [vuetify] },
    props: { zammadNr },
  });

describe("ZammadButton", () => {
  beforeEach(() => {
    useReferenceLink.mockReturnValue(mockUseReferenceLink);
  });

  test("renders zammad button with correct label and href", () => {
    const wrapper = createWrapper();
    const button = wrapper.findComponent({ name: "v-btn-primary" });
    expect(button.exists()).toBe(true);
    expect(button.text()).toBe("Zammad Ticket");
    expect(button.attributes("href")).toBe(
      "https://zammad.muenchen.invalid?nr=00000018"
    );
  });
});
