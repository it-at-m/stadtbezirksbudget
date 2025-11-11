import { mount } from "@vue/test-utils";
import { createPinia } from "pinia";
import { beforeEach, describe, expect, test, vi } from "vitest";
import { ref } from "vue";
import { createVuetify } from "vuetify";
import * as components from "vuetify/components";
import * as directives from "vuetify/directives";

import AntragStatusSelect from "@/components/AntragStatusSelect.vue";
import { useAntragStatusSelect } from "@/composables/antragStatusSelect";

vi.mock("@/composables/antragStatusSelect");

const pinia = createPinia();
const vuetify = createVuetify({ components, directives });

const createWrapper = () =>
  mount(AntragStatusSelect, {
    global: { plugins: [pinia, vuetify] },
    props: { antragId: "1", status: "EINGEGANGEN" },
  });

describe("AntragStatusSelect", () => {
  let mockUseAntragStatusSelect;

  beforeEach(() => {
    mockUseAntragStatusSelect = {
      status: ref("EINGEGANGEN"),
      statusOptions: ref([
        { title: "Offen", value: "EINGEGANGEN" },
        { title: "Abgelehnt", value: "ABGELEHNT" },
      ]),
      updateStatus: vi.fn(),
    };

    vi.mocked(useAntragStatusSelect).mockReturnValue(mockUseAntragStatusSelect);
  });

  test("testRendersAutocomplete", () => {
    const wrapper = createWrapper();

    expect(wrapper.find('[data-test="antrag-status-select"]').exists()).toBe(
      true
    );
  });

  test("testPassesStatusOptionsToAutocompleteItems", () => {
    const wrapper = createWrapper();

    const autocomplete = wrapper.findComponent({ name: "VAutocomplete" });
    expect(autocomplete.exists()).toBe(true);
    expect(autocomplete.props("items")).toEqual(
      mockUseAntragStatusSelect.statusOptions.value
    );
  });

  test("testCallsUpdateStatusWhenModelValueUpdates", async () => {
    const wrapper = createWrapper();

    const autocomplete = wrapper.findComponent({ name: "VAutocomplete" });
    expect(autocomplete.exists()).toBe(true);
    expect(mockUseAntragStatusSelect.status.value).toBe("EINGEGANGEN");

    await autocomplete.vm.$emit("update:model-value", "ABGELEHNT");

    expect(mockUseAntragStatusSelect.updateStatus).toHaveBeenCalledWith(
      "ABGELEHNT"
    );
  });
});
