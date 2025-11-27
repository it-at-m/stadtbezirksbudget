import { mount } from "@vue/test-utils";
import { createPinia } from "pinia";
import { beforeEach, describe, expect, test, vi } from "vitest";
import { ref } from "vue";
import { createVuetify } from "vuetify";
import * as components from "vuetify/components";
import * as directives from "vuetify/directives";
import { VDateInput } from "vuetify/labs/components";

import AntragListFilter from "@/components/AntragListFilter.vue";
import { useAntragListFilter } from "@/composables/antragListFilter.ts";
import { emptyAntragListFilter } from "@/types/AntragListFilter.ts";

vi.mock("@/composables/antragListFilter.ts");
global.ResizeObserver = class {
  observe() {
    // Mock implementation: No action needed
  }
  disconnect() {
    // Mock implementation: No action needed
  }
};

const pinia = createPinia();
const vuetify = createVuetify({
  components: { ...components, VDateInput },
  directives,
});

const inputFields = [
  {
    dataTest: "antrag-list-filter-status",
  },
  {
    dataTest: "antrag-list-filter-bezirksausschuss",
  },
  {
    dataTest: "antrag-list-filter-eingang-datum",
  },
  {
    dataTest: "antrag-list-filter-antragsteller-name",
  },
  {
    dataTest: "antrag-list-filter-projekt-titel",
  },
  {
    dataTest: "antrag-list-filter-beantragtes-budget-von",
  },
  {
    dataTest: "antrag-list-filter-beantragtes-budget-bis",
  },
  {
    dataTest: "antrag-list-filter-art",
  },
  {
    dataTest: "antrag-list-filter-aktualisierung-art",
  },
  {
    dataTest: "antrag-list-filter-aktualisierung-datum",
  },
];

describe("AntragListFilter", () => {
  let wrapper;
  let mockUseAntragListFilter;

  beforeEach(() => {
    mockUseAntragListFilter = {
      updateFilters: vi.fn(),
      resetFilters: vi.fn(),
      filters: ref(emptyAntragListFilter()),
    };

    vi.mocked(useAntragListFilter).mockReturnValue(mockUseAntragListFilter);

    wrapper = mount(AntragListFilter, {
      global: {
        plugins: [pinia, vuetify],
      },
    });
  });

  test("renders card", async () => {
    const content = wrapper.findComponent({ name: "VCard" });

    expect(content.exists()).toBe(true);
  });

  test("reset button triggers resetFilters", async () => {
    const button = wrapper.findComponent(
      '[data-test="antrag-list-filter-reset-btn"]'
    );

    expect(button.exists()).toBe(true);
    expect(button.classes()).toContain("v-btn");
    await button.trigger("click");
    expect(mockUseAntragListFilter.resetFilters).toHaveBeenCalled();
  });

  test.each(inputFields)(
    "renders input field $dataTest",
    async ({ dataTest }) => {
      const input = wrapper.findComponent(`[data-test="${dataTest}"]`);
      expect(input.exists()).toBe(true);

      await input.trigger("focusout");
      expect(mockUseAntragListFilter.updateFilters).toHaveBeenCalled();
    }
  );
});
