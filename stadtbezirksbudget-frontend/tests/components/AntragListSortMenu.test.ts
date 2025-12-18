import { mount } from "@vue/test-utils";
import { describe, expect, test, vi } from "vitest";

import AntragListSortMenu from "@/components/AntragListSortMenu.vue";
import vuetify from "@/plugins/vuetify.ts";
import { ResizeObserverMock } from "../_testUtils/ResizeObserverMock";

vi.stubGlobal("visualViewport", new EventTarget());
vi.stubGlobal("ResizeObserver", ResizeObserverMock);

describe("AntragListSortMenu", () => {
  const wrapper = mount(AntragListSortMenu, {
    global: {
      plugins: [vuetify],
      stubs: {
        AntragListSort: true,
      },
    },
  });

  test("renders menu and open button", async () => {
    const menu = wrapper.findComponent({ name: "VMenu" });
    const button = wrapper.findComponent(
      '[data-test="antrag-list-sort-open-btn"]'
    );

    expect(menu.exists()).toBe(true);
    expect(button.exists()).toBe(true);
  });

  test("shows AntragListSort on menu open", async () => {
    const button = wrapper.findComponent(
      '[data-test="antrag-list-sort-open-btn"]'
    );

    expect(wrapper.findComponent({ name: "AntragListSort" }).exists()).toBe(
      false
    );
    await button.trigger("click");
    expect(wrapper.findComponent({ name: "AntragListSort" }).exists()).toBe(
      true
    );
  });
});
