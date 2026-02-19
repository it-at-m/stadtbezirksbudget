import { mdiDotsVertical } from "@mdi/js";
import { mount } from "@vue/test-utils";
import { describe, expect, test, vi } from "vitest";

import EakteReference from "@/components/references/EakteReference.vue";
import vuetify from "@/plugins/vuetify.ts";
import { ResizeObserverMock } from "../../_testUtils/ResizeObserverMock";

vi.stubGlobal("visualViewport", new EventTarget());
vi.stubGlobal("ResizeObserver", ResizeObserverMock);

const eakteCooAdresse = "COO.1234.5678.9.0123456";
const aktenzeichen = "2025-00-00/12345";
const antragId = "10000000-0000-0000-0000-000000000049";
const createWrapper = () =>
  mount(EakteReference, {
    global: {
      plugins: [vuetify],
      stubs: {
        EakteLink: {
          name: "EakteLink",
          template: "<div></div>",
          props: ["aktenzeichen", "eakteCooAdresse"],
        },
        EakteCooAdresseEdit: {
          name: "EakteCooAdresseEdit",
          template: "<div></div>",
          props: ["antragId", "eakteCooAdresse"],
        },
      },
    },
    props: { antragId, aktenzeichen, eakteCooAdresse },
  });

describe("EakteReference", () => {
  test("renders menu with link and button", () => {
    const wrapper = createWrapper();
    const link = wrapper.findComponent({ name: "EakteLink" });
    const button = wrapper.findComponent({ name: "VBtn" });
    expect(link.exists()).toBe(true);
    expect(link.props("aktenzeichen")).toBe(aktenzeichen);
    expect(link.props("eakteCooAdresse")).toBe(eakteCooAdresse);
    expect(button.exists()).toBe(true);
    expect(button.props("icon")).toBe(mdiDotsVertical);
  });

  test("opens menu on button click", async () => {
    const wrapper = createWrapper();
    const menu = wrapper.findComponent({ name: "VMenu" });
    const button = wrapper.findComponent({ name: "VBtn" });

    expect(menu.props("modelValue")).toBe(false);
    expect(wrapper.findComponent({ name: "VList" }).exists()).toBe(false);

    await button.trigger("click");
    expect(menu.props("modelValue")).toBe(true);
    expect(wrapper.findComponent({ name: "VList" }).exists()).toBe(true);
  });

  test("renders menu content list", async () => {
    const wrapper = createWrapper();
    await wrapper.findComponent({ name: "VBtn" }).trigger("click");
    const list = wrapper.findComponent({ name: "VList" });
    expect(list.findComponent({ name: "EakteCooAdresseEdit" }).exists()).toBe(
      true
    );
  });

  test("closes menu on edit save", async () => {
    const wrapper = createWrapper();
    const menu = wrapper.findComponent({ name: "VMenu" });
    const button = wrapper.findComponent({ name: "VBtn" });

    await button.trigger("click");
    expect(menu.props("modelValue")).toBe(true);

    const cooEdit = wrapper.findComponent({ name: "EakteCooAdresseEdit" });
    await cooEdit.vm.$emit("save");
    expect(menu.props("modelValue")).toBe(false);
  });
});
