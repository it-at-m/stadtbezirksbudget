import type { VueWrapper } from "@vue/test-utils";

import { mount } from "@vue/test-utils";
import { beforeEach, describe, expect, test, vi } from "vitest";

import DetailsCard from "@/components/antragDetails/DetailsCard.vue";
import DetailsInformationenAntrag from "@/components/antragDetails/DetailsInformationenAntrag.vue";
import pinia from "@/plugins/pinia.ts";
import vuetify from "@/plugins/vuetify.ts";
import { ResizeObserverMock } from "../../_testUtils/ResizeObserverMock";

vi.stubGlobal("ResizeObserver", ResizeObserverMock);

const mockAntrag = {
  allgemein: {
    eingangDatum: "2024-10-01",
    status: "EINGEGANGEN",
    zammadTicketNr: "Z-123",
    aktenzeichen: "AZ-456",
    istGegendert: true,
    anmerkungen: "Anmerkungen",
  },
  antragsteller: {
    name: "Mustermann",
  },
  finanzierung: {
    beantragtesBudget: 1234,
  },
  projekt: {
    titel: "Projekt Titel",
    rubrik: "Rubrik",
  },
};

const fields = [
  {
    dataTest: "projekt-titel",
    expected: mockAntrag.projekt.titel,
    component: "VTextField",
  },
  {
    dataTest: "eingang-datum",
    expected: new Date(mockAntrag.allgemein.eingangDatum).toLocaleDateString(
      "de-DE",
      {
        day: "2-digit",
        month: "2-digit",
        year: "numeric",
      }
    ),
    component: "VDateInput",
  },
  {
    dataTest: "antragsteller-name",
    expected: mockAntrag.antragsteller.name,
    component: "VTextField",
  },
  {
    dataTest: "beantragtes-budget",
    expected: mockAntrag.finanzierung.beantragtesBudget.toLocaleString(
      "de-De",
      {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2,
        useGrouping: false,
      }
    ),
    component: "VNumberInput",
  },
  {
    dataTest: "rubrik",
    expected: mockAntrag.projekt.rubrik,
    component: "VTextField",
  },
  {
    dataTest: "status",
    expected: mockAntrag.allgemein.status,
    component: "StatusSelect",
  },
  {
    dataTest: "zammad-nr",
    expected: mockAntrag.allgemein.zammadTicketNr,
    component: "VTextField",
  },
  {
    dataTest: "aktenzeichen",
    expected: mockAntrag.allgemein.aktenzeichen,
    component: "VTextField",
  },
  {
    dataTest: "ist-gegendert",
    expected: mockAntrag.allgemein.istGegendert,
    component: "VCheckbox",
  },
  {
    dataTest: "anmerkungen",
    expected: mockAntrag.allgemein.anmerkungen,
    component: "VTextarea",
  },
];

describe("DetailsInformationenAntrag", () => {
  let wrapper: VueWrapper<InstanceType<typeof DetailsInformationenAntrag>>;
  let card: VueWrapper<InstanceType<typeof DetailsCard>>;

  beforeEach(() => {
    wrapper = mount(DetailsInformationenAntrag, {
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
