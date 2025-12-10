import { shallowMount } from "@vue/test-utils";
import { describe, expect, test } from "vitest";

import TheSnackbar from "@/components/TheSnackbar.vue";
import i18n from "@/plugins/i18n";
import pinia from "@/plugins/pinia.ts";
import vuetify from "@/plugins/vuetify.ts";

describe("TheSnackbar", () => {
  test("renders snackbar with message", () => {
    const message = "Hello_World";
    const wrapper = shallowMount(TheSnackbar, {
      global: {
        plugins: [pinia, vuetify, i18n],
      },
      props: { message: message },
    });
    expect(wrapper.html()).toContain(message);
  });
});
