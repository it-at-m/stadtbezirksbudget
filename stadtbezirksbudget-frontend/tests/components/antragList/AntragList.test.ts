import { mount } from "@vue/test-utils";
import { beforeEach, describe, expect, test, vi } from "vitest";
import { ref } from "vue";

import AntragList from "@/components/antragList/AntragList.vue";
import { useAntragList } from "@/composables/useAntragList.ts";
import pinia from "@/plugins/pinia.ts";
import vuetify from "@/plugins/vuetify.ts";
import { ResizeObserverMock } from "../../_testUtils/ResizeObserverMock.ts";

vi.mock("@/composables/useAntragList.ts");

vi.stubGlobal("ResizeObserver", ResizeObserverMock);

describe("AntragList", () => {
  let wrapper;
  let mockUseAntragList;

  beforeEach(() => {
    mockUseAntragList = {
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
          finanzierungArt: "FEST",
          aktualisierung: "ZAMMAD",
          aktualisierungDatum: "2025-01-15",
        },
      ]),
      totalItems: ref(1),
      page: ref(1),
      itemsPerPage: ref(10),
      loading: ref(false),
      updateOptions: vi.fn(),
      goToDetails: vi.fn(),
    };

    vi.mocked(useAntragList).mockReturnValue(mockUseAntragList);

    wrapper = mount(AntragList, {
      global: {
        plugins: [pinia, vuetify],
      },
    });
  });

  test("renders antrag summary list", () => {
    expect(wrapper.find('[data-test="antrag-list"]').exists()).toBe(true);
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
    expect(wrapper.find('[data-test="item-finanzierung-art"]').exists()).toBe(
      true
    );
    expect(wrapper.find('[data-test="item-aktualisierung-art"]').exists()).toBe(
      true
    );
    expect(wrapper.find('[data-test="item-zammad-nr"]').exists()).toBe(true);
    expect(wrapper.find('[data-test="item-aktenzeichen"]').exists()).toBe(true);
  });

  test("updates ui on screen size change", async () => {
    const originalInnerWidth = window.innerWidth;

    const initialWidth = 1200;
    Object.defineProperty(window, "innerWidth", {
      writable: true,
      value: initialWidth,
    });

    wrapper = mount(AntragList, {
      global: {
        plugins: [vuetify],
      },
    });

    await wrapper.vm.$nextTick();

    const expectedInitialMaxWidth = (initialWidth * 0.95) / 11;
    expect(wrapper.vm.computedHeaders).toHaveLength(11);
    expect(wrapper.vm.computedHeaders[0].maxWidth).toBeDefined();
    const initialMaxWidth = parseFloat(
      wrapper.vm.computedHeaders[0].maxWidth.replace("px", "")
    );

    expect(initialMaxWidth).toBeCloseTo(expectedInitialMaxWidth, 1);

    const newWidth = 800;
    Object.defineProperty(window, "innerWidth", {
      writable: true,
      value: newWidth,
    });
    window.dispatchEvent(new Event("resize"));

    await new Promise((resolve) => setTimeout(resolve, 200));
    await wrapper.vm.$nextTick();

    const expectedNewMaxWidth = (newWidth * 0.95) / 11;
    const newMaxWidth = parseFloat(
      wrapper.vm.computedHeaders[0].maxWidth.replace("px", "")
    );

    expect(newMaxWidth).toBeCloseTo(expectedNewMaxWidth, 1);
    expect(newMaxWidth).not.toBe(initialMaxWidth);

    const expectedItemSelectors = [
      "item-status",
      "item-aktualisierung-datum",
      "item-aktualisierung-art",
      "item-eingang-datum",
      "item-beantragtes-budget",
      "item-finanzierung-art",
    ];

    expectedItemSelectors.forEach((selector) => {
      expect(wrapper.find(`[data-test="${selector}"]`).exists()).toBe(true);
    });

    Object.defineProperty(window, "innerWidth", {
      writable: true,
      value: originalInnerWidth,
    });
  });

  test("renders antrag summary items", async () => {
    mockUseAntragList.items.value = [
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
        finanzierungArt: "FEST",
        aktualisierung: "FACHANWENDUNG",
        aktualisierungDatum: "2025-01-16",
      },
    ];

    await wrapper.vm.$nextTick();

    const statusElement = wrapper.find('[data-test="item-status"]');
    expect(statusElement.exists()).toBe(true);
    expect(statusElement.text()).toBe("Offen");

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

    const aktualisierungArtElement = wrapper.find(
      '[data-test="item-aktualisierung-art"]'
    );

    expect(aktualisierungArtElement.exists()).toBe(true);
    expect(aktualisierungArtElement.text()).toBe("Fachanwendung");

    const beantragtesBudgetElement = wrapper.find(
      '[data-test="item-beantragtes-budget"]'
    );
    expect(beantragtesBudgetElement.exists()).toBe(true);
    expect(beantragtesBudgetElement.text()).toBe("7.500");

    const finanzierungArtElement = wrapper.find(
      '[data-test="item-finanzierung-art"]'
    );
    expect(finanzierungArtElement.exists()).toBe(true);
    expect(finanzierungArtElement.text()).toBe("Fest");
  });

  test("updates props on pagination options change", async () => {
    mockUseAntragList.page.value = 2;
    mockUseAntragList.itemsPerPage.value = 5;

    await wrapper.vm.$nextTick();

    const dataTable = wrapper.findComponent({ name: "VDataTableServer" });
    expect(dataTable.props("page")).toBe(2);
    expect(dataTable.props("itemsPerPage")).toBe(5);
  });

  test("emits update:options event on pagination change", async () => {
    const dataTable = wrapper.findComponent({ name: "VDataTableServer" });

    dataTable.vm.$emit("update:options", {
      page: 3,
      itemsPerPage: 20,
    });

    await wrapper.vm.$nextTick();

    expect(mockUseAntragList.updateOptions).toHaveBeenCalledWith({
      page: 3,
      itemsPerPage: 20,
    });
  });

  test("calls goToDetails on row click by emitting table event", async () => {
    const dataTable = wrapper.findComponent({ name: "VDataTableServer" });
    dataTable.vm.$emit("click:row", new MouseEvent("click"), {
      item: mockUseAntragList.items.value[0],
    });
    await wrapper.vm.$nextTick();

    expect(mockUseAntragList.goToDetails).toHaveBeenCalledWith(
      expect.any(MouseEvent),
      { item: mockUseAntragList.items.value[0] }
    );
  });
});
