import type { VueWrapper } from "@vue/test-utils";

import { mount } from "@vue/test-utils";
import { beforeEach, describe, expect, test, vi } from "vitest";
import { nextTick } from "vue";

import DetailsHeader from "@/components/antragDetails/DetailsHeader.vue";
import pinia from "@/plugins/pinia.ts";
import vuetify from "@/plugins/vuetify.ts";
import { ResizeObserverMock } from "../../_testUtils/ResizeObserverMock";

vi.stubGlobal("ResizeObserver", ResizeObserverMock);

const mockAntrag = {
  allgemeineInformationen: {
    zammadNr: "1234",
  },
};

describe("DetailsHeader", () => {
  let wrapper: VueWrapper<InstanceType<typeof DetailsHeader>>;
  beforeEach(() => {
    wrapper = mount(DetailsHeader, {
      global: {
        plugins: [pinia, vuetify],
        stubs: {
          ZammadButton: {
            name: "ZammadButton",
            props: ["zammadNr"],
            template: "<div>{{ zammadNr }}</div>",
          },
        },
      },
      props: { antrag: mockAntrag },
    });
  });

  test("renders zammad button", () => {
    const zammadButton = wrapper.findComponent({ name: "ZammadButton" });
    expect(zammadButton.exists()).toBe(true);
  });

  test("passes zammadNr to zammad button", () => {
    const zammadButton = wrapper.findComponent({ name: "ZammadButton" });
    expect(zammadButton.props("zammadNr")).toBe(
      mockAntrag.allgemeineInformationen.zammadNr
    );
    expect(zammadButton.text()).toBe(
      mockAntrag.allgemeineInformationen.zammadNr
    );
  });

  test("updates zammadNr when antrag prop changes", async () => {
    const newAntrag = {
      allgemeineInformationen: {
        ...mockAntrag.allgemeineInformationen,
        zammadNr: "4567",
      },
    };
    await wrapper.setProps({ antrag: newAntrag });
    await nextTick();

    const zammadButton = wrapper.findComponent({ name: "ZammadButton" });
    expect(zammadButton.props("zammadNr")).toBe("4567");
  });
});
