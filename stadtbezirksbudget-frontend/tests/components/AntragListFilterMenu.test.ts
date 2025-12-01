import { mount } from "@vue/test-utils";
import { describe, expect, test, vi } from "vitest";
import { createVuetify } from "vuetify";
import * as components from "vuetify/components";
import * as directives from "vuetify/directives";

import AntragListFilterMenu from "@/components/AntragListFilterMenu.vue";

vi.stubGlobal("visualViewport", new EventTarget());
global.ResizeObserver = class {
  observe() {
    // Mock implementation: No action needed
  }
  disconnect() {
    // Mock implementation: No action needed
  }
};

const vuetify = createVuetify({
  components,
  directives,
});

describe("AntragListFilterMenu", () => {
  const wrapper = mount(AntragListFilterMenu, {
    global: {
      plugins: [vuetify],
      stubs: {
        AntragListFilter: true,
      },
    },
  });

  test("renders menu and open button", async () => {
    const menu = wrapper.findComponent({ name: "VMenu" });
    const button = wrapper.findComponent(
      '[data-test="antrag-list-filter-open-btn"]'
    );

    expect(menu.exists()).toBe(true);
    expect(button.exists()).toBe(true);
  });

  test("shows AntragListFilter on menu open", async () => {
    const button = wrapper.findComponent(
      '[data-test="antrag-list-filter-open-btn"]'
    );

    expect(wrapper.findComponent({ name: "AntragListFilter" }).exists()).toBe(
      false
    );
    await button.trigger("click");
    expect(wrapper.findComponent({ name: "AntragListFilter" }).exists()).toBe(
      true
    );
  });
});
