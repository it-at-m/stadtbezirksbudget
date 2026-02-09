import { mount } from "@vue/test-utils";
import { describe, expect, test } from "vitest";

import EakteButton from "@/components/references/EakteButton.vue";
import vuetify from "@/plugins/vuetify.ts";
import { eakteToLink } from "@/util/formatter";

const eakteCooAdresse = "COO.1234.5678.9.0123456";

describe("EakteButton", () => {
  const wrapper = mount(EakteButton, {
    global: {
      plugins: [vuetify],
    },
    props: { eakteCooAdresse },
  });

  test("renders eakte button with correct label and href", () => {
    const button = wrapper.findComponent({ name: "v-btn-primary" });
    expect(button.exists()).toBe(true);
    expect(button.text()).toBe("E-Akte öffnen");
    expect(button.attributes("href")).toBe(eakteToLink(eakteCooAdresse));
  });
});
