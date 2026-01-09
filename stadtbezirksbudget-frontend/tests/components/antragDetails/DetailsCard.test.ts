import type { VueWrapper } from "@vue/test-utils";

import { mount } from "@vue/test-utils";
import { beforeEach, describe, expect, test } from "vitest";

import DetailsCard from "@/components/antragDetails/DetailsCard.vue";
import pinia from "@/plugins/pinia.ts";
import vuetify from "@/plugins/vuetify.ts";

describe("DetailsCard", () => {
  let wrapper: VueWrapper<InstanceType<typeof DetailsCard>>;
  let vCard: VueWrapper<Element>;
  let vCardTitle: VueWrapper<Element>;
  let vCardText: VueWrapper<Element>;

  beforeEach(() => {
    wrapper = mount(DetailsCard, {
      global: { plugins: [pinia, vuetify] },
      props: { title: "Test Titel" },
      slots: { default: '<p data-test="slot">Slot Content</p>' },
    });
    vCard = wrapper.findComponent({ name: "VCard" });
    vCardTitle = vCard.findComponent({ name: "VCardTitle" });
    vCardText = vCard.findComponent({ name: "VCardText" });
  });

  test("renders v-card", () => {
    expect(vCard.exists()).toBe(true);
    expect(vCardTitle.exists()).toBe(true);
    expect(vCardText.exists()).toBe(true);
  });

  test("renders title", () => {
    const title = vCardTitle.find("h2");
    expect(title.exists()).toBe(true);
    expect(title.text()).toBe("Test Titel");
  });

  test("renders edit button", () => {
    const btn = vCardTitle.findComponent({ name: "VBtn" });
    expect(btn.exists()).toBe(true);
  });

  test("renders slot content", () => {
    const slot = vCardText.find("[data-test=slot]");
    expect(slot.exists()).toBe(true);
    expect(slot.text()).toBe("Slot Content");
  });
});
