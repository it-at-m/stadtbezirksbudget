import type { VueWrapper } from "@vue/test-utils";

import { mount } from "@vue/test-utils";
import { createPinia } from "pinia";
import { beforeEach, describe, expect, test, vi } from "vitest";
import { ref } from "vue";
import { createVuetify } from "vuetify";
import * as components from "vuetify/components";
import * as directives from "vuetify/directives";
import { VDateInput } from "vuetify/labs/components";

import AntragListFilter from "@/components/AntragListFilter.vue";
import { useAntragListFilter } from "@/composables/useAntragListFilter.ts";
import { emptyAntragListFilter } from "@/types/AntragListFilter.ts";

vi.mock("@/composables/useAntragListFilter.ts");
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
    subFields: ["range-input-from", "range-input-to"],
  },
  {
    dataTest: "antrag-list-filter-antragsteller-name",
  },
  {
    dataTest: "antrag-list-filter-projekt-titel",
  },
  {
    dataTest: "antrag-list-filter-beantragtes-budget",
    subFields: ["range-input-from", "range-input-to"],
  },
  {
    dataTest: "antrag-list-filter-art",
  },
  {
    dataTest: "antrag-list-filter-aktualisierung-art",
  },
  {
    dataTest: "antrag-list-filter-aktualisierung-datum",
    subFields: ["range-input-from", "range-input-to"],
  },
];

describe("AntragListFilter", () => {
  let wrapper: VueWrapper<InstanceType<typeof AntragListFilter>>;
  let mockUseAntragListFilter: ReturnType<typeof useAntragListFilter>;

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
    async ({ dataTest, subFields }) => {
      const input = wrapper.findComponent(`[data-test="${dataTest}"]`);
      expect(input.exists()).toBe(true);

      if (subFields) {
        for (const subField of subFields) {
          const subInput = input.findComponent(`[data-test="${subField}"]`);
          expect(subInput.exists()).toBe(true);
          await subInput.trigger("focusout");
          expect(mockUseAntragListFilter.updateFilters).toHaveBeenCalled();
        }
        return;
      }

      await input.trigger("focusout");
      expect(mockUseAntragListFilter.updateFilters).toHaveBeenCalled();
    }
  );
});
