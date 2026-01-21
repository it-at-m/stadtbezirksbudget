import { VBtn } from "vuetify/components";

export const componentAliases = {
  VBtnPrimary: VBtn,
  VBtnSecondary: VBtn,
  VBtnTertiary: VBtn,
};
const commonBtnStyle = {
  density: "comfortable",
  size: "default",
  rounded: "lg",
  variant: "elevated",
};
export const defaults = {
  VBtnPrimary: {
    color: "#006A63",
    ...commonBtnStyle,
  },
  VBtnSecondary: {
    color: "#D4F9F4",
    ...commonBtnStyle,
  },
  VBtnTertiary: {
    color: "#47617A",
    ...commonBtnStyle,
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
