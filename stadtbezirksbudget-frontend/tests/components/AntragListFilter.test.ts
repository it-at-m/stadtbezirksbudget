import { mount } from "@vue/test-utils";
import { createPinia } from "pinia";
import { beforeEach, describe, expect, test, vi } from "vitest";
import { ref } from "vue";
import { createVuetify } from "vuetify";
import * as components from "vuetify/components";
import * as directives from "vuetify/directives";

import AntragListFilter from "@/components/AntragListFilter.vue";
import { useAntragListFilter } from "@/composables/antragListFilter.ts";
import { emptyAntragListFilter } from "@/types/AntragListFilter.ts";

vi.mock("@/composables/antragListFilter.ts");

const pinia = createPinia();
const vuetify = createVuetify({ components, directives });

describe("AntragStatusUpdate", () => {
  let wrapper;
  let mockUseAntragListFilter;

  beforeEach(() => {
    mockUseAntragListFilter = {
      status: ref("EINGEGANGEN"),
      search: ref(""),
      updateStatus: vi.fn(),
      toggleStatusAndSearch: vi.fn(),
      updateFilters: vi.fn(),
      resetFilters: vi.fn(),
      filters: emptyAntragListFilter(),
    };

    vi.mocked(useAntragListFilter).mockReturnValue(mockUseAntragListFilter);

    wrapper = mount(AntragListFilter, {
      attachTo: document.body,
      global: {
        plugins: [pinia, vuetify],
      },
    });
  });

  async function openMenu() {
    const menu = wrapper.findComponent({ name: "VMenu" });
    menu.vm.$emit("update:modelValue", true);
    // await wrapper.vm.$nextTick();
  }

  test("renders menu and open button", () => {
    const menu = wrapper.findComponent({ name: "VMenu" });
    const button = wrapper.find('[data-test="antrag-list-filter-open-btn"]');
    const content = wrapper.find(
      '[data-test="antrag-list-filter-menu-content"]'
    );

    expect(menu.exists()).toBe(true);
    expect(button.exists()).toBe(true);
    expect(content.exists()).toBe(false);

    button.trigger("click");
    wrapper.vm.$nextTick().then(() => {
      expect(content.exists()).toBe(true);
    });
  });

  // test("renders reset button", () => {
  //   openMenu();
  //   wrapper.vm.$nextTick().then(() => {
  //     const button = wrapper.find('[data-test="antrag-list-filter-reset-btn"]');
  //     expect(button.exists()).toBe(true);
  //
  //     button.trigger("click");
  //     expect(mockUseAntragListFilter.resetFilters).toHaveBeenCalled();
  //   });
  // });

  // const inputFields = [
  //   {
  //     dataTest: "antrag-list-filter-status",
  //   },
  //   {
  //     dataTest: "antrag-list-filter-bezirksausschuss",
  //   },
  // ];

  // test.each(inputFields)("renders input field $dataTest", ({ dataTest }) => {
  //   openMenu().then(() => {
  //     const input = wrapper.find(`[data-test="${dataTest}"]`);
  //     expect(input.exists()).toBe(true);
  //
  //     // input.vm.$emit("update:modelValue", "TEST_VALUE");
  //     // wrapper.vm.$nextTick().then(() => {
  //     //   expect(mockUseAntragListFilter.updateFilters).toHaveBeenCalled();
  //     // });
  //   });
  // });
});
