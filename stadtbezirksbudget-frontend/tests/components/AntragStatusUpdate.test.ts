import { mount } from "@vue/test-utils";
import { beforeEach, describe, expect, test, vi } from "vitest";
import { ref } from "vue";

import AntragStatusUpdate from "@/components/AntragStatusUpdate.vue";
import { useAntragStatusUpdate } from "@/composables/useAntragStatusUpdate.ts";
import pinia from "@/plugins/pinia.ts";
import vuetify from "@/plugins/vuetify.ts";
import { statusOptions } from "../../src/types/Status";

vi.mock("@/composables/useAntragStatusUpdate.ts");

const createWrapper = () =>
  mount(AntragStatusUpdate, {
    global: { plugins: [pinia, vuetify] },
    props: { antragId: "1", initialStatus: "EINGEGANGEN" },
  });

describe("AntragStatusUpdate", () => {
  let mockUseAntragStatusUpdate;

  beforeEach(() => {
    mockUseAntragStatusUpdate = {
      status: ref("EINGEGANGEN"),
      search: ref(""),
      updateStatus: vi.fn(),
      toggleStatusAndSearch: vi.fn(),
    };

    vi.mocked(useAntragStatusUpdate).mockReturnValue(mockUseAntragStatusUpdate);
  });

  test("renders antrag status select", () => {
    const wrapper = createWrapper();

    expect(wrapper.find('[data-test="antrag-status-select"]').exists()).toBe(
      true
    );
  });

  test("renders autocomplete with status options", () => {
    const wrapper = createWrapper();

    const autocomplete = wrapper.findComponent({ name: "VAutocomplete" });
    expect(autocomplete.exists()).toBe(true);
    expect(autocomplete.props("items")).toEqual(statusOptions);
  });

  test("calls updateStatus when modelValue updates", async () => {
    const wrapper = createWrapper();

    const autocomplete = wrapper.findComponent({ name: "VAutocomplete" });
    expect(autocomplete.exists()).toBe(true);
    expect(mockUseAntragStatusUpdate.status.value).toBe("EINGEGANGEN");
    await autocomplete.vm.$emit("update:modelValue", "ABGELEHNT");

    expect(mockUseAntragStatusUpdate.updateStatus).toHaveBeenCalledWith(
      "ABGELEHNT"
    );
  });

  test("calls toggleStatusAndSearch when focused", async () => {
    const wrapper = createWrapper();

    const autocomplete = wrapper.findComponent({ name: "VAutocomplete" });

    await autocomplete.vm.$emit("update:focused", true);

    expect(
      mockUseAntragStatusUpdate.toggleStatusAndSearch
    ).toHaveBeenCalledWith(true);
  });

  test("calls toggleStatusAndSearch when unfocused", async () => {
    const wrapper = createWrapper();

    const autocomplete = wrapper.findComponent({ name: "VAutocomplete" });

    await autocomplete.vm.$emit("update:focused", false);

    expect(
      mockUseAntragStatusUpdate.toggleStatusAndSearch
    ).toHaveBeenCalledWith(false);
  });
});
