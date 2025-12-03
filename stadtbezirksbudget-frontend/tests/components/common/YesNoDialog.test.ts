import { mount } from "@vue/test-utils";
import { describe, expect, test } from "vitest";
import { createVuetify } from "vuetify";
import * as components from "vuetify/components";
import * as directives from "vuetify/directives";

import YesNoDialog from "@/components/common/YesNoDialog.vue";

const vuetify = createVuetify({ components, directives });

describe("YesNoDialog", () => {
  test("renders activator", () => {
    const wrapper = mount(YesNoDialog, {
      props: {
        modelValue: false,
        buttontext: "Löschen",
        dialogtitle: "Titel",
        dialogtext: "Text",
      },
      global: { plugins: [vuetify] },
    });

    const activator = wrapper.find("[data-test='activator-button']");
    expect(activator.exists()).toBe(true);
  });

  test("renders icon", () => {
    const wrapper = mount(YesNoDialog, {
      props: {
        modelValue: false,
        icontext: "mdi-delete",
        dialogtitle: "Titel",
        dialogtext: "Text",
      },
      global: {
        plugins: [vuetify],
      },
    });

    const icon = wrapper.find(".v-icon");
    expect(icon.exists()).toBe(true);
    expect(icon.classes()).toContain("mdi-delete");
  });

  test("emits events on button clicks", async () => {
    const wrapper = mount(YesNoDialog, {
      props: {
        modelValue: false,
        buttontext: "Löschen",
        dialogtitle: "Titel",
        dialogtext: "Text",
      },
      global: {
        plugins: [vuetify],
        stubs: {
          "v-dialog": { template: "<div><slot /> </div>" },
        },
      },
    });

    await wrapper.vm.$nextTick();

    const yes = wrapper.find("[data-test='yes-button']");
    const no = wrapper.find("[data-test='no-button']");

    expect(yes.exists()).toBe(true);
    expect(no.exists()).toBe(true);

    await yes.trigger("click");
    await no.trigger("click");

    const emitted = wrapper.emitted();
    expect(emitted).toBeTruthy();
    expect(emitted?.yes).toBeTruthy();
    expect(emitted?.yes.length).toBe(1);
    expect(emitted?.no).toBeTruthy();
    expect(emitted?.no.length).toBe(1);
  });
});
