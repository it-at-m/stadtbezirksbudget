import { mount } from "@vue/test-utils";
import { describe, expect, test } from "vitest";

import EakteLink from "@/components/references/EakteLink.vue";
import vuetify from "@/plugins/vuetify.ts";
import { eakteToLink } from "@/util/formatter";

const eakteCooAdresse = "COO.1234.5678.9.0123456";
const aktenzeichen = "2025-00-00/12345";

describe("EakteLink", () => {
  const wrapper = mount(EakteLink, {
    global: {
      plugins: [vuetify],
    },
    props: { eakteCooAdresse, aktenzeichen },
  });

  test("renders a link with aktenzeichen and correct href", () => {
    const link = wrapper.find("a");
    expect(link.exists()).toBe(true);
    expect(link.text()).toBe(aktenzeichen);
    expect(link.attributes("href")).toBe(eakteToLink(eakteCooAdresse));
  });
});
