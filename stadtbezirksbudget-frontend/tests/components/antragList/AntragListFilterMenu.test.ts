import { mount } from "@vue/test-utils";
import { describe, expect, test, vi } from "vitest";

import AntragListFilterMenu from "@/components/antragList/AntragListFilterMenu.vue";
import vuetify from "@/plugins/vuetify.ts";
import { ResizeObserverMock } from "../../_testUtils/ResizeObserverMock.ts";

vi.stubGlobal("visualViewport", new EventTarget());

vi.stubGlobal("ResizeObserver", ResizeObserverMock);

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
