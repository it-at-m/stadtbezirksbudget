import type { VueWrapper } from "@vue/test-utils";

import { mount } from "@vue/test-utils";
import { beforeEach, describe, expect, test, vi } from "vitest";

import DetailsCard from "@/components/antragDetails/DetailsCard.vue";
import InformationenAntrag from "@/components/antragDetails/InformationenAntrag.vue";
import pinia from "@/plugins/pinia.ts";
import vuetify from "@/plugins/vuetify.ts";
import { ResizeObserverMock } from "../../_testUtils/ResizeObserverMock";

vi.stubGlobal("ResizeObserver", ResizeObserverMock);

const mockAntrag = {
  projektTitel: "Projekt Titel",
  eingangDatum: "2024-10-01",
  antragstellerName: "Max Mustermann",
  beantragtesBudget: 1234,
  rubrik: "Rubrik",
  status: "EINGEGANGEN",
  zammadNr: "Z-123",
  aktenzeichen: "AZ-456",
  istGegendert: true,
  anmerkungen: "Anmerkungen",
};

const fields = [
  {
    dataTest: "projekt-titel",
    expected: mockAntrag.projektTitel,
    component: "VTextField",
  },
  {
    dataTest: "eingang-datum",
    expected: new Date(mockAntrag.eingangDatum).toLocaleDateString("de-DE", {
      day: "2-digit",
      month: "2-digit",
      year: "numeric",
    }),
    component: "VDateInput",
  },
  {
    dataTest: "antragsteller-name",
    expected: mockAntrag.antragstellerName,
    component: "VTextField",
  },
  {
    dataTest: "beantragtes-budget",
    expected: String(mockAntrag.beantragtesBudget),
    component: "VNumberInput",
  },
  {
    dataTest: "rubrik",
    expected: mockAntrag.rubrik,
    component: "VTextField",
  },
  {
    dataTest: "status",
    expected: mockAntrag.status,
    component: "StatusSelect",
  },
  {
    dataTest: "zammad-nr",
    expected: mockAntrag.zammadNr,
    component: "VTextField",
  },
  {
    dataTest: "aktenzeichen",
    expected: mockAntrag.aktenzeichen,
    component: "VTextField",
  },
  {
    dataTest: "ist-gegendert",
    expected: mockAntrag.istGegendert,
    component: "VCheckbox",
  },
  {
    dataTest: "anmerkungen",
    expected: mockAntrag.anmerkungen,
    component: "VTextarea",
  },
];

describe("InformationenAntrag.vue", () => {
  let wrapper: VueWrapper<InstanceType<typeof InformationenAntrag>>;
  let card: VueWrapper<InstanceType<typeof DetailsCard>>;

  beforeEach(() => {
    wrapper = mount(InformationenAntrag, {
      global: { plugins: [pinia, vuetify] },
      props: { antrag: mockAntrag },
    });
    card = wrapper.findComponent({ name: "DetailsCard" });
  });

  test("renders details card", () => {
    expect(card.exists()).toBe(true);
    expect(card.props("title")).toBe("Informationen Antrag");
  });

  test.each(fields)(
    "renders field $dataTest with expected value",
    ({ dataTest, expected, component }) => {
      const field = card.findComponent(`[data-test="${dataTest}"]`);
      expect(field.exists()).toBe(true);
      const fieldComponent = field.findComponent({
        name: component,
      });
      expect(fieldComponent.exists()).toBe(true);
      expect(fieldComponent.props("modelValue")).toBe(expected);
    }
  );
});
