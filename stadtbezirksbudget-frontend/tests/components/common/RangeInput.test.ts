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

  test("uses default input labels", () => {
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
    expect(fromInput.props("label")).toBe("von");
    expect(toInput.props("label")).toBe("bis");
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

    const inputs = wrapper.findAllComponents(InputStub);
    expect(inputs.length).toBe(2);
    const [fromInput, toInput] = inputs;
    expect(fromInput.props("label")).toBe("From Label");
    expect(toInput.props("label")).toBe("To Label");
    expect(fromInput.attributes()["data-test"]).toBe("range-input-from");
    expect(toInput.attributes()["data-test"]).toBe("range-input-to");
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

  test("accepts initial values via from/to props", () => {
    const wrapper = mount(RangeInput, {
      props: {
        label: "Range Label",
        inputComponent: InputStub,
        from: "initial-from",
        to: "initial-to",
      },
      global: {
        plugins: [pinia, vuetify],
      },
    });

    const [fromInput, toInput] = wrapper.findAllComponents(InputStub);
    expect(fromInput.props("modelValue")).toBe("initial-from");
    expect(toInput.props("modelValue")).toBe("initial-to");
  });
});
