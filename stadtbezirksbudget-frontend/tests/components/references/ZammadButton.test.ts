import { mount } from "@vue/test-utils";
import { describe, expect, test } from "vitest";

import ZammadButton from "@/components/references/ZammadButton.vue";
import vuetify from "@/plugins/vuetify.ts";
import { zammadNrToLink } from "@/util/formatter";

const zammadNr = "00000018";

describe("ZammadButton", () => {
  const wrapper = mount(ZammadButton, {
    global: {
      plugins: [vuetify],
    },
    props: { zammadNr },
  });

  test("renders zammad button with correct label and href", () => {
    const button = wrapper.findComponent({ name: "v-btn" });
    expect(button.exists()).toBe(true);
    expect(button.text()).toBe("Zammad Ticket");
    expect(button.attributes("href")).toBe(zammadNrToLink(zammadNr));
  });
});
