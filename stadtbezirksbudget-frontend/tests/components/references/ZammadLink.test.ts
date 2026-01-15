import { mount } from "@vue/test-utils";
import { describe, expect, test } from "vitest";

import ZammadLink from "@/components/references/ZammadLink.vue";
import vuetify from "@/plugins/vuetify.ts";
import { zammadNrToLink } from "@/util/formatter";

const zammadNr = "00000018";

describe("ZammadLink", () => {
  const wrapper = mount(ZammadLink, {
    global: {
      plugins: [vuetify],
    },
    props: { zammadNr },
  });

  test("renders a link with zammad number and correct href", () => {
    const link = wrapper.find("a");
    expect(link.exists()).toBe(true);
    expect(link.text()).toBe(zammadNr);
    expect(link.attributes("href")).toBe(zammadNrToLink(zammadNr));
  });
});
