// @ts-expect-error: "TS2307 cannot find module" is a false positive here
import "vuetify/styles";

import type { VueI18nAdapterParams } from "vuetify/locale/adapters/vue-i18n";

import { useI18n } from "vue-i18n";
import { createVuetify } from "vuetify";
import { aliases, mdi } from "vuetify/iconsets/mdi-svg";
import { VDateInput } from "vuetify/labs/components";
import { createVueI18nAdapter } from "vuetify/locale/adapters/vue-i18n";

import i18n from "@/plugins/i18n";

export default createVuetify({
  components: {
    VDateInput,
  },
  icons: {
    defaultSet: "mdi",
    aliases,
    sets: {
      mdi,
    },
  },
  theme: {
    themes: {
      light: {
        colors: {
          background: "#F4FBF9",
          surface: "#FFFFFF",
          "surface-dim": "D5DBD9",
          "surface-variant": "333333",
          primary: "#006A63",
          "primary-container": "#D4F9F4", // custom
          secondary: "#4A6360",
          "secondary-container": "#CBDCE3", // custom
          tertiary: "#47617A", // custom
          "tertiary-container": "#CEE5FF", // custom
          success: "#69BE28",
          error: "#C62828",
        },
      },
    },
  },
  locale: {
    adapter: createVueI18nAdapter({ i18n, useI18n } as VueI18nAdapterParams),
  },
});
