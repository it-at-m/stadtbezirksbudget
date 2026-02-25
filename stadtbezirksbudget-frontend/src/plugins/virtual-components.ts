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
  style: "text-transform: none",
};
export const defaults = {
  VBtnPrimary: {
    ...commonBtnStyle,
    color: "#006A63",
  },
  VBtnSecondary: {
    ...commonBtnStyle,
    color: "#006A63",
    variant: "outlined",
  },
  VBtnTertiary: {
    ...commonBtnStyle,
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
