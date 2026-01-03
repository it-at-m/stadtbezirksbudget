import type { VueWrapper } from "@vue/test-utils";

import { mount } from "@vue/test-utils";
import { beforeEach, describe, expect, test, vi } from "vitest";
import { ref } from "vue";

import AntragListSearch from "@/components/AntragListSearch.vue";
import { useAntragListSearch } from "@/composables/useAntragListSearch.ts";
import pinia from "@/plugins/pinia.ts";
import vuetify from "@/plugins/vuetify.ts";

vi.mock("@/composables/useAntragListSearch.ts");

describe("AntragListSearch", () => {
  let wrapper: VueWrapper<InstanceType<typeof AntragListSearch>>;
  let mockUseAntragListSearch: ReturnType<typeof useAntragListSearch>;

  beforeEach(() => {
    mockUseAntragListSearch = {
      search: vi.fn(),
      query: ref("test"),
    };

    vi.mocked(useAntragListSearch).mockReturnValue(mockUseAntragListSearch);

    wrapper = mount(AntragListSearch, {
      global: {
        plugins: [pinia, vuetify],
      },
    });
  });

  test("renders search", async () => {
    const search = wrapper.findComponent({ name: "VTextField" });

    expect(search.exists()).toBe(true);
    expect(search.props().label).toBe("Suche");
    expect(search.props().modelValue).toBe("test");
  });

  test("triggers search on enter", async () => {
    const search = wrapper.findComponent({ name: "VTextField" });

    await search.trigger("keyup.enter");
    expect(mockUseAntragListSearch.search).toHaveBeenCalled();
  });

  test("triggers search on focusout", async () => {
    const search = wrapper.findComponent({ name: "VTextField" });

    await search.trigger("focusout");
    expect(mockUseAntragListSearch.search).toHaveBeenCalled();
  });

  test("triggers search on clear", async () => {
    const search = wrapper.findComponent({ name: "VTextField" });
    const clearIcon = search.find(".v-field__clearable .v-icon--clickable");
    expect(clearIcon.exists()).toBe(true);

    await clearIcon.trigger("click");
    expect(mockUseAntragListSearch.query.value).toBe(null);
    expect(mockUseAntragListSearch.search).toHaveBeenCalled();
  });
});
