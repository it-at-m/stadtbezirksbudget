import { VBtn } from "vuetify/components";

export const componentAliases = {
  VBtnPrimary: VBtn,
  VBtnSecondary: VBtn,
  VBtnTertiary: VBtn,
};
export const defaults = {
  VBtn: {
    density: "comfortable",
    rounded: "lg",
  },
  VBtnPrimary: {
    color: "#006A63",
  },
  VBtnSecondary: {
    variants: "tonal",
    color: "#D4F9F4",
  },
  VBtnTertiary: {
    color: "#47617A",
  },
  VNumberInput: {
    variant: "outlined",
    controlVariant: "hidden",
  },
  VTextField: {
    variant: "outlined",
  },
  VTextarea: {
    variant: "outlined",
    autoGrow: true,
  },
};
