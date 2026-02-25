import type { VueWrapper } from "@vue/test-utils";

import { mount } from "@vue/test-utils";
import { beforeEach, describe, expect, test, vi } from "vitest";

import ActiveFilterNotice from "@/components/antragList/ActiveFilterNotice.vue";
import vuetify from "@/plugins/vuetify.ts";

vi.stubGlobal("visualViewport", new EventTarget());

describe("ActiveFilterNotice", () => {
  let wrapper: VueWrapper<InstanceType<typeof ActiveFilterNotice>>;

  beforeEach(() => {
    wrapper = mount(ActiveFilterNotice, {
      global: { plugins: [vuetify] },
      stubs: {
        VSnackbar: {
          name: "VSnackbar",
          template: "<div><slot /></div>",
        },
      },
    });
  });

  test("changes v-model depending on active prop", async () => {
    await wrapper.setProps({ active: false });
    expect(wrapper.vm.show).toBe(false);

    const snackbar = wrapper.findComponent({ name: "VSnackbar" });
    expect(snackbar.exists()).toBe(true);
    expect(snackbar.props("modelValue")).toBe(false);

    await wrapper.setProps({ active: true });
    expect(wrapper.vm.show).toBe(true);
    expect(snackbar.props("modelValue")).toBe(true);
  });

  test("closes snackbar on button click", async () => {
    await wrapper.setProps({ active: true });
    const snackbar = wrapper.findComponent({ name: "VSnackbar" });
    const button = wrapper.findComponent({ name: "VBtn" });
    expect(button.text()).toBe("Schließen");

    await button.trigger("click");
    expect(snackbar.props("modelValue")).toBe(false);
  });
});
