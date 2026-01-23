import { mount } from "@vue/test-utils";
import { beforeEach, describe, expect, test, vi } from "vitest";
import { ref } from "vue";

import TheAppBar from "@/components/TheAppBar.vue";
import { useRouteCheck } from "@/composables/useRouteCheck.ts";
import pinia from "@/plugins/pinia.ts";
import vuetify from "@/plugins/vuetify.ts";
import { useUserStore } from "@/stores/useUserStore.ts";
import { UserLocalDevelopment } from "@/types/User.ts";

vi.mock("@/composables/useRouteCheck.ts");
vi.mock("@/stores/useUserStore.ts");

describe("TheAppBar", () => {
  function mountComponent() {
    return mount(TheAppBar, {
      global: {
        plugins: [pinia, vuetify],
        stubs: {
          VAppBar: { template: "<div><slot /></div>" },
          RouterLink: { template: "<a><slot/></a>" },
        },
      },
    });
  }

  beforeEach(() => {
    useRouteCheck.mockReturnValue({
      isHomePage: ref(false),
    });
    useUserStore.mockReturnValue({
      getUser: null,
    });
  });

  test("renders app title", () => {
    const wrapper = mountComponent();

    const title = wrapper.findComponent({ name: "VToolbarTitle" });
    expect(title.exists()).toBe(true);
    expect(title.text()).toBe("Stadtbezirksbudget");
  });

  test("renders navigation icon that emits on click", () => {
    const wrapper = mountComponent();

    const navIcon = wrapper.findComponent({ name: "VAppBarNavIcon" });
    expect(navIcon.exists()).toBe(true);
    navIcon.trigger("click");
    expect(wrapper.emitted()).toHaveProperty("toggleNavigationDrawer");
  });

  test("renders app switcher", () => {
    const wrapper = mountComponent();

    const appSwitcher = wrapper.findComponent({ name: "AppSwitcher" });
    expect(appSwitcher.exists()).toBe(true);
  });

  test("doesn't render user avatar on null user", () => {
    useUserStore.mockReturnValue({
      getUser: null,
    });
    const wrapper = mountComponent();

    const userAvatar = wrapper.findComponent({ name: "Ad2ImageAvatar" });
    expect(userAvatar.exists()).toBe(false);
  });

  test("renders user avatar on not null user", () => {
    useUserStore.mockReturnValue({
      getUser: UserLocalDevelopment(),
    });
    const wrapper = mountComponent();

    const userAvatar = wrapper.findComponent({ name: "Ad2ImageAvatar" });
    expect(userAvatar.exists()).toBe(true);
  });

  test("renders list options on home page", () => {
    useRouteCheck.mockReturnValue({
      isHomePage: ref(true),
    });
    const wrapper = mountComponent();

    const search = wrapper.findComponent({ name: "AntragListSearch" });
    const sortMenu = wrapper.findComponent({ name: "AntragListSortMenu" });
    const filterMenu = wrapper.findComponent({ name: "AntragListFilterMenu" });

    expect(search.exists()).toBe(true);
    expect(sortMenu.exists()).toBe(true);
    expect(filterMenu.exists()).toBe(true);
  });

  test("doesn't render list options outside home page", () => {
    useRouteCheck.mockReturnValue({
      isHomePage: ref(false),
    });
    const wrapper = mountComponent();

    const search = wrapper.findComponent({ name: "AntragListSearch" });
    const sortMenu = wrapper.findComponent({ name: "AntragListSortMenu" });
    const filterMenu = wrapper.findComponent({ name: "AntragListFilterMenu" });

    expect(search.exists()).toBe(false);
    expect(sortMenu.exists()).toBe(false);
    expect(filterMenu.exists()).toBe(false);
  });
});
