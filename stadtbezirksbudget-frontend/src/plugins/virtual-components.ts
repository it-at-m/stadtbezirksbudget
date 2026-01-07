import { VBtn } from "vuetify/components";

export const aliases = {
  VBtnPrimary: VBtn,
  VBtnSecondary: VBtn,
  VBtnTertiary: VBtn,
};
export const virtualComponents = {
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
};
