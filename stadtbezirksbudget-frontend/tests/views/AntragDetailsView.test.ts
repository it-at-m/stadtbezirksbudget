import { mount } from "@vue/test-utils";
import { beforeEach, describe, expect, test, vi } from "vitest";
import { ref } from "vue";
import { useRoute } from "vue-router";

import { useAntragDetails } from "@/composables/useAntragDetails.ts";
import pinia from "@/plugins/pinia.ts";
import vuetify from "@/plugins/vuetify.ts";
import AntragDetailsView from "@/views/AntragDetailsView.vue";
import { ResizeObserverMock } from "../_testUtils/ResizeObserverMock";

vi.mock("@/composables/useAntragDetails.ts");
vi.mock("vue-router", async (importOriginal) => {
  return {
    ...(await importOriginal()),
    useRoute: vi.fn(),
  };
});

vi.stubGlobal("ResizeObserver", ResizeObserverMock);

const cards = ["DetailsHeader", "DetailsInformationenAntrag"];

function createCardStub(name: string) {
  return {
    props: ["antrag"],
    template: `<div data-test="card-${name}">{{ antrag?.allgemein.projektTitel }}</div>`,
  };
}

function mountViewWithStubs(cardNames: string[]) {
  const stubs = Object.fromEntries(
    cardNames.map((n) => [n, createCardStub(n)])
  );

  return mount(AntragDetailsView, {
    global: {
      plugins: [pinia, vuetify],
      stubs,
    },
  });
}

describe("AntragDetailsView", () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  test("calls useAntragDetails with correct parameters", () => {
    const mockDetails = ref({
      allgemein: { projektTitel: "Projekt Test" },
    });

    vi.mocked(useAntragDetails).mockReturnValue({
      details: mockDetails,
    });
    vi.mocked(useRoute).mockReturnValue({
      params: { id: "123" },
    } as unknown as ReturnType<typeof useRoute>);

    mountViewWithStubs([]);

    expect(useAntragDetails).toHaveBeenCalledWith("123");
  });

  test("renders container and cards when details are available", () => {
    const mockDetails = ref({
      allgemein: { projektTitel: "Projekt Test" },
    });

    vi.mocked(useAntragDetails).mockReturnValue({ details: mockDetails });
    vi.mocked(useRoute).mockReturnValue({
      params: { id: "123" },
    } as unknown as ReturnType<typeof useRoute>);

    const wrapper = mountViewWithStubs(cards);

    expect(wrapper.findComponent({ name: "VContainer" }).exists()).toBe(true);
    expect(wrapper.findAllComponents({ name: "VRow" }).length).toBe(
      cards.length
    );
    expect(wrapper.findAllComponents({ name: "VCol" }).length).toBe(
      cards.length
    );
  });

  test.each(cards)("renders %s when details are available", (cardName) => {
    const mockDetails = ref({
      allgemein: { projektTitel: "Projekt Test" },
    });

    vi.mocked(useAntragDetails).mockReturnValue({ details: mockDetails });
    vi.mocked(useRoute).mockReturnValue({
      params: { id: "123" },
    } as unknown as ReturnType<typeof useRoute>);

    const wrapper = mountViewWithStubs([cardName]);

    const node = wrapper.find(`[data-test="card-${cardName}"]`);
    expect(node.exists()).toBe(true);
    expect(node.text()).toContain("Projekt Test");
  });

  test("does not render container when details is undefined", () => {
    const mockDetails = ref(undefined);

    vi.mocked(useAntragDetails).mockReturnValue({ details: mockDetails });
    vi.mocked(useRoute).mockReturnValue({
      params: { id: "123" },
    } as unknown as ReturnType<typeof useRoute>);

    const wrapper = mountViewWithStubs(cards);

    expect(wrapper.findComponent({ name: "VContainer" }).exists()).toBe(false);
  });
});
