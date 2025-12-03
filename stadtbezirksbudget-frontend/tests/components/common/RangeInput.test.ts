import { mount } from "@vue/test-utils";
import { describe, expect, test } from "vitest";
import { markRaw } from "vue";

import RangeInput from "@/components/common/RangeInput.vue";
import pinia from "@/plugins/pinia";
import vuetify from "@/plugins/vuetify";

describe("RangeInput", () => {
  const InputStub = markRaw({
    name: "InputStub",
    props: ["modelValue", "label"],
    emits: ["update:modelValue"],
    template: `<input v-bind="$attrs" :value="modelValue" @input="$emit('update:modelValue', $event.target.value)" />`,
  });

  test("renders label", () => {
    const wrapper = mount(RangeInput, {
      props: {
        label: "Range Label",
        inputComponent: InputStub,
      },
      global: {
        plugins: [pinia, vuetify],
      },
    });

    const label = wrapper.find('[data-test="range-input-label"]');
    expect(label.exists()).toBe(true);
    expect(label.text()).toContain("Range Label");
  });

  test("renders two inputs", () => {
    const wrapper = mount(RangeInput, {
      props: {
        label: "Range Label",
        inputComponent: InputStub,
        fromLabel: "From Label",
        toLabel: "To Label",
      },
      global: {
        plugins: [pinia, vuetify],
      },
    });

    const fromInput = wrapper.find('[data-test="range-input-from"]');
    const toInput = wrapper.find('[data-test="range-input-to"]');
    expect(fromInput.exists()).toBe(true);
    expect(toInput.exists()).toBe(true);

    const inputs = wrapper.findAllComponents(InputStub);
    expect(inputs.length).toBe(2);
    expect(inputs[0].props("label")).toBe("From Label");
    expect(inputs[1].props("label")).toBe("To Label");

    expect(fromInput.html()).toBe(inputs[0].html());
    expect(toInput.html()).toBe(inputs[1].html());
  });

  test("forwards inputProps", () => {
    const wrapper = mount(RangeInput, {
      props: {
        label: "Range Label",
        inputComponent: InputStub,
        inputProps: { "data-foo": "bar" },
      },
      global: {
        plugins: [pinia, vuetify],
      },
    });

    const fromInput = wrapper.find('[data-test="range-input-from"]');
    const toInput = wrapper.find('[data-test="range-input-to"]');
    expect(fromInput.attributes()["data-foo"]).toBe("bar");
    expect(toInput.attributes()["data-foo"]).toBe("bar");
  });

  test("updates defineModel refs when child emits update:modelValue", async () => {
    const wrapper = mount(RangeInput, {
      props: {
        label: "Range Label",
        inputComponent: InputStub,
      },
      global: {
        plugins: [pinia, vuetify],
      },
    });

    const [fromInput, toInput] = wrapper.findAllComponents(InputStub);
    await fromInput.setValue("10");
    await toInput.setValue("20");

    expect(wrapper.vm.fromModel).toBe("10");
    expect(wrapper.vm.toModel).toBe("20");
  });
});
