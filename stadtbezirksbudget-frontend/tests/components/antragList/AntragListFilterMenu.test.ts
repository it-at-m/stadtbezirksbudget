import { mount } from "@vue/test-utils";
import { describe, expect, test, vi } from "vitest";
import { computed } from "vue";

import AntragListFilterMenu from "@/components/antragList/AntragListFilterMenu.vue";
import { useAntragListFilter } from "@/composables/useAntragListFilter.ts";
import vuetify from "@/plugins/vuetify.ts";
import { ResizeObserverMock } from "../../_testUtils/ResizeObserverMock.ts";

vi.mock("@/composables/useAntragListFilter.ts");
vi.stubGlobal("visualViewport", new EventTarget());
vi.stubGlobal("ResizeObserver", ResizeObserverMock);

function createWrapper(hasActiveFilters: boolean) {
  vi.mocked(useAntragListFilter).mockReturnValue({
    hasActiveFilters: () => computed(() => hasActiveFilters),
  });

  return mount(AntragListFilterMenu, {
    global: {
      plugins: [vuetify],
      stubs: { AntragListFilter: true },
    },
  });
}

describe("AntragListFilterMenu", () => {
  test("renders menu, open button and notice", async () => {
    const wrapper = createWrapper(false);
    const menu = wrapper.findComponent({ name: "VMenu" });
    const button = wrapper.findComponent({
      name: "VBtn",
      props: { "data-test": "antrag-list-filter-open-btn" },
    });

    expect(menu.exists()).toBe(true);
    expect(button.exists()).toBe(true);
    expect(button.props("variant")).toBe("elevated");

    const notice = wrapper.findComponent({ name: "ActiveFilterNotice" });
    expect(notice.props("active")).toBe(false);
  });

  test("changes button and notice props if hasActiveFilters", () => {
    const wrapper = createWrapper(true);
    const button = wrapper.findComponent({
      name: "VBtn",
      props: { "data-test": "antrag-list-filter-open-btn" },
    });
    expect(button.props("variant")).toBe("outlined");

    const notice = wrapper.findComponent({ name: "ActiveFilterNotice" });
    expect(notice.props("active")).toBe(true);
  });

  test("shows AntragListFilter on menu open", async () => {
    const wrapper = createWrapper(false);
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
