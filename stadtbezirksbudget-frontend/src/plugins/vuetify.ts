// @ts-expect-error: "TS2307 cannot find module" is a false positive here
import "vuetify/styles";

import type { VueI18nAdapterParams } from "vuetify/locale/adapters/vue-i18n";

import { useI18n } from "vue-i18n";
import { createVuetify } from "vuetify";
import { aliases, mdi } from "vuetify/iconsets/mdi-svg";
import { VDateInput } from "vuetify/labs/components";
import { createVueI18nAdapter } from "vuetify/locale/adapters/vue-i18n";

import i18n from "@/plugins/i18n";
import { componentAliases, defaults } from "./virtual-components.ts";

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
  aliases: componentAliases,
  defaults,
  theme: {
    themes: {
      light: {
        colors: {
          primary: "#006A63",
          "primary-variant": "#1ec6ba",
          secondary: "#D4F9F4",
          tertiary: "#47617A",
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
