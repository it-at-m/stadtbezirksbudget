import { mount } from "@vue/test-utils";
import { useToggle } from "@vueuse/core";
import { beforeEach, describe, expect, test, vi } from "vitest";
import { ref } from "vue";

import App from "@/App.vue";
import { useInitializeStores } from "@/composables/useInitializeStores";
import pinia from "@/plugins/pinia";
import vuetify from "@/plugins/vuetify";
import { ResizeObserverMock } from "./_testUtils/ResizeObserverMock";

vi.mock("@/composables/useInitializeStores");
vi.mock("@vueuse/core");

vi.stubGlobal("ResizeObserver", ResizeObserverMock);

describe("App", () => {
  const components = [
    "TheSnackbar",
    "TheAppBar",
    "VMain",
    "VContainer",
    "RouterView",
  ];
  const stubs = ["TheSnackbar", "TheAppBar", "RouterView"];

  beforeEach(() => {
    vi.clearAllMocks();
    vi.mocked(useToggle).mockReturnValue([ref(false), vi.fn()]);
  });

  test.each(components)("renders %s component", (componentName) => {
    const wrapper = mount(App, {
      global: {
        plugins: [pinia, vuetify],
        stubs,
      },
    });

    const component = wrapper.findComponent({ name: componentName });
    expect(component.exists()).toBe(true);
  });

  test("calls useInitializeStores on setup", () => {
    mount(App, {
      global: {
        plugins: [pinia, vuetify],
        stubs,
      },
    });

    expect(vi.mocked(useInitializeStores)).toHaveBeenCalled();
  });
});
