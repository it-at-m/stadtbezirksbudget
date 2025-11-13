import { mount } from "@vue/test-utils";
import { describe, expect, test } from "vitest";
import { createVuetify } from "vuetify";
import * as components from "vuetify/components";
import * as directives from "vuetify/directives";

import YesNoDialog from "@/components/common/YesNoDialog.vue";

const vuetify = createVuetify({ components, directives });

describe("YesNoDialog", () => {
  test("testDialogRendersActivator", async () => {
    const wrapper = mount(YesNoDialog, {
      props: {
        modelValue: false,
        buttontext: "Löschen",
        dialogtitle: "Titel",
        dialogtext: "Text",
      },
      global: { plugins: [vuetify] },
    });
    expect(wrapper.html()).toContain("Löschen");
  });

  test("testRendersIcon", async () => {
    const wrapper = mount(YesNoDialog, {
      props: {
        modelValue: false,
        icontext: "mdi-delete",
        dialogtitle: "Titel",
        dialogtext: "Text",
      },
      global: { plugins: [vuetify] },
    });

    expect(wrapper.html()).toContain("mdi-delete");
  });

  test("testEmitsOnClicks", async () => {
    const wrapper = mount(YesNoDialog, {
      props: {
        modelValue: false,
        buttontext: "Löschen",
        dialogtitle: "Titel",
        dialogtext: "Text",
      },
      global: { plugins: [vuetify] },
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
    expect(emitted?.no).toBeTruthy();
  });
});
