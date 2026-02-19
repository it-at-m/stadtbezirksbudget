import { mount } from "@vue/test-utils";
import { beforeEach, describe, expect, test, vi } from "vitest";
import { computed, MaybeRefOrGetter, toValue } from "vue";

import ZammadLink from "@/components/references/ZammadLink.vue";
import { useReferenceLink } from "@/composables/useReferenceLink.ts";
import vuetify from "@/plugins/vuetify.ts";

vi.mock("@/composables/useReferenceLink.ts");
const mockUseReferenceLink = {
  getZammadLink: (nr: MaybeRefOrGetter<string>) =>
    computed(() => `https://zammad.muenchen.invalid?nr=${toValue(nr)}`),
};

const zammadNr = "00000018";
const createWrapper = () =>
  mount(ZammadLink, {
    global: { plugins: [vuetify] },
    props: { zammadNr },
  });

describe("ZammadLink", () => {
  beforeEach(() => {
    useReferenceLink.mockReturnValue(mockUseReferenceLink);
  });

  test("renders a link with zammad number and correct href", () => {
    const wrapper = createWrapper();
    const link = wrapper.find("a");
    expect(link.exists()).toBe(true);
    expect(link.text()).toBe(zammadNr);
    expect(link.attributes("href")).toBe(
      "https://zammad.muenchen.invalid?nr=00000018"
    );
  });
});
