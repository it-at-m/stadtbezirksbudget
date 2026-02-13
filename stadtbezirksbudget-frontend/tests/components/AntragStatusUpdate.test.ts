import { mount } from "@vue/test-utils";
import { beforeEach, describe, expect, test, vi } from "vitest";
import { nextTick, ref } from "vue";

import AntragStatusUpdate from "@/components/AntragStatusUpdate.vue";
import { useAntragStatusUpdate } from "@/composables/useAntragStatusUpdate.ts";
import pinia from "@/plugins/pinia.ts";
import vuetify from "@/plugins/vuetify.ts";
import { Status, statusOptions } from "@/types/Status";

vi.mock("@/composables/useAntragStatusUpdate.ts");

const createWrapper = () =>
  mount(AntragStatusUpdate, {
    global: { plugins: [pinia, vuetify] },
    props: { antragId: "1", initialStatus: "EINGEGANGEN" },
  });

describe("AntragStatusUpdate", () => {
  let mockUseAntragStatusUpdate: ReturnType<typeof useAntragStatusUpdate> & {
    onStatusUpdated: (newStatus: Status) => void;
  };

  beforeEach(() => {
    mockUseAntragStatusUpdate = {
      status: ref("EINGEGANGEN"),
      search: ref(""),
      updateStatus: vi.fn(),
      toggleStatusAndSearch: vi.fn(),
      onStatusUpdated: vi.fn(),
    };

    vi.mocked(useAntragStatusUpdate).mockImplementation(
      (
        _antragId: string,
        _initialStatus: Status,
        onStatusUpdated: (newStatus: Status) => void
      ) => {
        mockUseAntragStatusUpdate.onStatusUpdated = onStatusUpdated;
        return mockUseAntragStatusUpdate;
      }
    );
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

  test("emits status-updated on callback call", async () => {
    const wrapper = createWrapper();

    mockUseAntragStatusUpdate.onStatusUpdated("ABGELEHNT");
    await nextTick();

    const emitted = wrapper.emitted("status-updated");
    expect(emitted).toBeTruthy();
    expect(emitted?.[0]).toEqual(["ABGELEHNT"]);
  });
});
