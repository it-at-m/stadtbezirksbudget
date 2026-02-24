import type { VueWrapper } from "@vue/test-utils";

import { mount } from "@vue/test-utils";
import { beforeEach, describe, expect, test, vi } from "vitest";
import { nextTick } from "vue";

import DetailsHeader from "@/components/antragDetails/DetailsHeader.vue";
import { ROUTES_HOME } from "@/constants.ts";
import pinia from "@/plugins/pinia.ts";
import router from "@/plugins/router.ts";
import vuetify from "@/plugins/vuetify.ts";
import { ResizeObserverMock } from "../../_testUtils/ResizeObserverMock";

vi.mock("@/plugins/router.ts", () => ({ default: { push: vi.fn() } }));
vi.stubGlobal("ResizeObserver", ResizeObserverMock);

const mockAntrag = {
  allgemein: {
    zammadNr: "1234",
    eakteCooAdresse: "COO.1234.5678.9.0123456",
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
          EakteButton: {
            name: "EakteButton",
            props: ["eakteCooAdresse"],
            template: "<div>{{ eakteCooAdresse }}</div>",
          },
        },
      },
      props: { antrag: mockAntrag },
    });
  });

  describe("BackButton", () => {
    test("renders back button", () => {
      const backButton = wrapper.find("[data-test=details-back-button]");
      expect(backButton.exists()).toBe(true);
      expect(backButton.text()).toBe("Zurück zur Übersicht");
    });

    test("navigates to home on back button click", async () => {
      (router.push as vi.Mock).mockResolvedValue(undefined);
      const backButton = wrapper.find("[data-test=details-back-button]");
      await backButton.trigger("click");
      expect(router.push).toHaveBeenCalledWith({ name: ROUTES_HOME });
    });
  });

  describe("ZammadButton", () => {
    test("renders zammad button", () => {
      const zammadButton = wrapper.findComponent({ name: "ZammadButton" });
      expect(zammadButton.exists()).toBe(true);
    });

    test("passes zammadNr to zammad button", () => {
      const zammadButton = wrapper.findComponent({ name: "ZammadButton" });
      expect(zammadButton.props("zammadNr")).toBe(
        mockAntrag.allgemein.zammadNr
      );
      expect(zammadButton.text()).toBe(mockAntrag.allgemein.zammadNr);
    });

    test("updates zammadNr when antrag prop changes", async () => {
      const newAntrag = {
        allgemein: {
          ...mockAntrag.allgemein,
          zammadNr: "4567",
        },
      };
      await wrapper.setProps({ antrag: newAntrag });
      await nextTick();

      const zammadButton = wrapper.findComponent({ name: "ZammadButton" });
      expect(zammadButton.props("zammadNr")).toBe("4567");
    });
  });

  describe("EakteButton", () => {
    test("renders eakte button", () => {
      const eakteButton = wrapper.findComponent({ name: "EakteButton" });
      expect(eakteButton.exists()).toBe(true);
    });

    test("passes eakteCooAdresse to eakte button", () => {
      const eakteButton = wrapper.findComponent({ name: "EakteButton" });
      expect(eakteButton.props("eakteCooAdresse")).toBe(
        mockAntrag.allgemein.eakteCooAdresse
      );
      expect(eakteButton.text()).toBe(mockAntrag.allgemein.eakteCooAdresse);
    });

    test("updates eakteCooAdresse when antrag prop changes", async () => {
      const newAntrag = {
        allgemein: {
          ...mockAntrag.allgemein,
          eakteCooAdresse: "COO.2234.5678.9.0123456",
        },
      };
      await wrapper.setProps({ antrag: newAntrag });
      await nextTick();

      const eakteButton = wrapper.findComponent({ name: "EakteButton" });
      expect(eakteButton.props("eakteCooAdresse")).toBe(
        "COO.2234.5678.9.0123456"
      );
    });
  });
});
