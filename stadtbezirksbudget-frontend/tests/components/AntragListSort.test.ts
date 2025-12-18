import type { VueWrapper } from "@vue/test-utils";

import { mount } from "@vue/test-utils";
import { beforeEach, describe, expect, test, vi } from "vitest";
import { ref } from "vue";

import AntragListSort from "@/components/AntragListSort.vue";
import { useAntragListSort } from "@/composables/useAntragListSort";
import pinia from "@/plugins/pinia.ts";
import vuetify from "@/plugins/vuetify.ts";
import { createEmptyListSort } from "@/types/AntragListSort";
import { ResizeObserverMock } from "../_testUtils/ResizeObserverMock";

vi.mock("@/composables/useAntragListSort.ts", () => ({
  useAntragListSort: vi.fn(),
}));

vi.stubGlobal("ResizeObserver", ResizeObserverMock);

const inputFields = [
  "antrag-list-sort-status",
  "antrag-list-sort-zammad-nr",
  "antrag-list-sort-aktenzeichen",
  "antrag-list-sort-bezirksausschuss-nr",
  "antrag-list-sort-eingang-datum",
  "antrag-list-sort-antragsteller-name",
  "antrag-list-sort-projekt-titel",
  "antrag-list-sort-beantragtes-budget",
  "antrag-list-sort-finanzierung-art",
  "antrag-list-sort-aktualisierung",
  "antrag-list-sort-aktualisierung-datum",
];

describe("AntragListSort", () => {
  let wrapper: VueWrapper<InstanceType<typeof AntragListSort>>;
  let mockUseAntragListSort: ReturnType<typeof useAntragListSort>;

  beforeEach(() => {
    mockUseAntragListSort = {
      updateSorting: vi.fn(),
      resetSorting: vi.fn(),
      sorting: ref(createEmptyListSort()),
    };

    vi.mocked(useAntragListSort).mockReturnValue(mockUseAntragListSort);

    wrapper = mount(AntragListSort, {
      global: {
        plugins: [pinia, vuetify],
      },
    });
  });

  test("renders card", async () => {
    const content = wrapper.findComponent({ name: "VCard" });

    expect(content.exists()).toBe(true);
  });

  test("reset button triggers resetSorting", async () => {
    const button = wrapper.findComponent(
      '[data-test="antrag-list-sort-reset-btn"]'
    );

    expect(button.exists()).toBe(true);
    expect(button.classes()).toContain("v-btn");
    await button.trigger("click");
    expect(mockUseAntragListSort.resetSorting).toHaveBeenCalled();
  });

  test.each(inputFields)("renders input field %s", async (dataTest) => {
    const input = wrapper.findComponent(`[data-test="${dataTest}"]`);
    expect(input.exists()).toBe(true);

    const selectedValue = {
      title: "Test",
      sortBy: "test",
      sortDirection: "asc",
    };

    await input.setValue(selectedValue);
    expect(mockUseAntragListSort.updateSorting).toHaveBeenCalled();
    expect(mockUseAntragListSort.updateSorting).toHaveBeenCalledWith(
      selectedValue
    );
  });
});
