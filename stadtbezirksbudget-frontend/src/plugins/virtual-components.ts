import { VBtn } from "vuetify/components";

export const componentAliases = {
  VBtnPrimary: VBtn,
  VBtnSecondary: VBtn,
  VBtnTertiary: VBtn,
};
export const defaults = {
  VBtn: {
    density: "comfortable",
    size: "default",
    rounded: "lg",
    variant: "elevated",
  },
  VBtnPrimary: {
    color: "#006A63",
  },
  VBtnSecondary: {
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
  VDateInput: {
    variant: "outlined",
    prependIcon: "",
  },
};
