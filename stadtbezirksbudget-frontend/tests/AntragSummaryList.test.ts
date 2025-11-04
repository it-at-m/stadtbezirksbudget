import { mount } from "@vue/test-utils";
import { beforeEach, describe, expect, test, vi } from "vitest";
import { ref } from "vue";
import { createVuetify } from "vuetify";
import * as components from "vuetify/components";
import * as directives from "vuetify/directives";

import AntragSummaryList from "@/components/AntragSummaryList.vue";
import { useAntragSummaryList } from "@/composables/useAntragSummaryList";

vi.mock("@/composables/useAntragSummaryList.ts");

global.ResizeObserver = class {
  observe() {
    // Mock implementation: No action needed
  }
  disconnect() {
    // Mock implementation: No action needed
  }
};

const vuetify = createVuetify({
  components,
  directives,
});

describe("AntragSummaryList", () => {
  let wrapper;
  let mockUseAntragSummaryList;

  beforeEach(() => {
    mockUseAntragSummaryList = {
      items: ref([
        {
          id: "1",
          status: "EINGEGANGEN",
          zammadNr: "Z-001",
          aktenzeichen: "AZ-001",
          bezirksausschussNr: 1,
          eingangDatum: "2025-01-15",
          antragstellerName: "Test Antragsteller",
          projektTitel: "Test Projekt",
          beantragtesBudget: 5000,
          istFehlbetrag: false,
          aktualisierung: "Erstellt",
          aktualisierungDatum: "2025-01-15",
        },
      ]),
      totalItems: ref(1),
      page: ref(1),
      itemsPerPage: ref(10),
      loading: ref(false),
      updateOptions: vi.fn(),
    };

    vi.mocked(useAntragSummaryList).mockReturnValue(mockUseAntragSummaryList);

    wrapper = mount(AntragSummaryList, {
      global: {
        plugins: [vuetify],
      },
    });
  });

  test("testRenderingAntragSummaryList", () => {
    expect(wrapper.find('[data-test="antrag-summary-list"]').exists()).toBe(
      true
    );
    expect(
      wrapper.find('[data-test="header-beantragtes-budget"]').exists()
    ).toBe(true);
    expect(wrapper.find('[data-test="item-status"]').exists()).toBe(true);
    expect(wrapper.find('[data-test="item-eingang-datum"]').exists()).toBe(
      true
    );
    expect(
      wrapper.find('[data-test="item-aktualisierung-datum"]').exists()
    ).toBe(true);
    expect(wrapper.find('[data-test="item-beantragtes-budget"]').exists()).toBe(
      true
    );
    expect(wrapper.find('[data-test="item-ist-fehlbetrag"]').exists()).toBe(
      true
    );
  });

  test("testUpdatingUiOptionsChange", async () => {
    mockUseAntragSummaryList.page.value = 2;
    mockUseAntragSummaryList.itemsPerPage.value = 5;

    await wrapper.vm.$nextTick();

    expect(wrapper.vm.page).toBe(2);
    expect(wrapper.vm.itemsPerPage).toBe(5);
  });

  test("testUpdatingUiScreenSizeChange", async () => {
    const originalInnerWidth = window.innerWidth;
    Object.defineProperty(window, "innerWidth", { writable: true, value: 800 });
    window.dispatchEvent(new Event("resize"));

    await wrapper.vm.$nextTick();

    const expectedHeaders = [
      "header-beantragtes-budget",
      "item-status",
      "item-eingang-datum",
      "item-aktualisierung-datum",
      "item-beantragtes-budget",
      "item-ist-fehlbetrag",
    ];

    expectedHeaders.forEach((header) => {
      expect(wrapper.find(`[data-test="${header}"]`).exists()).toBe(true);
    });

    expect(
      wrapper.find('[data-test="antrag-summary-list"]').element.scrollWidth
    ).toBeLessThanOrEqual(
      wrapper.find('[data-test="antrag-summary-list"]').element.clientWidth
    );

    Object.defineProperty(window, "innerWidth", {
      writable: true,
      value: originalInnerWidth,
    });
  });

  test("testUpdatingUiItemsChange", async () => {
    mockUseAntragSummaryList.items.value = [
      {
        id: "2",
        status: "EINGEGANGEN",
        zammadNr: "Z-002",
        aktenzeichen: "AZ-002",
        bezirksausschussNr: 2,
        eingangDatum: "2025-01-16",
        antragstellerName: "Test Antragsteller 2",
        projektTitel: "Test Projekt 2",
        beantragtesBudget: 7500,
        istFehlbetrag: false,
        aktualisierung: "Aktualisiert",
        aktualisierungDatum: "2025-01-16",
      },
    ];

    await wrapper.vm.$nextTick();

    const statusElement = wrapper.find('[data-test="item-status"]');
    expect(statusElement.exists()).toBe(true);
    expect(statusElement.text()).toBe("Eingegangen");

    const eingangDatumElement = wrapper.find(
      '[data-test="item-eingang-datum"]'
    );
    expect(eingangDatumElement.exists()).toBe(true);
    expect(eingangDatumElement.text()).toBe("16.01.2025");

    const aktualisierungDatumElement = wrapper.find(
      '[data-test="item-aktualisierung-datum"]'
    );
    expect(aktualisierungDatumElement.exists()).toBe(true);
    expect(aktualisierungDatumElement.text()).toBe("16.01.2025");

    const beantragtesBudgetElement = wrapper.find(
      '[data-test="item-beantragtes-budget"]'
    );
    expect(beantragtesBudgetElement.exists()).toBe(true);
    expect(beantragtesBudgetElement.text()).toBe("7.500");

    const istFehlbetragElement = wrapper.find(
      '[data-test="item-ist-fehlbetrag"]'
    );
    expect(istFehlbetragElement.exists()).toBe(true);
    expect(istFehlbetragElement.text()).toBe("Fest");
  });
});
