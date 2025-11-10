import type { Theme } from "vitepress";

import { useData } from "vitepress";
import { createMermaidRenderer } from "vitepress-mermaid-renderer";
import DefaultTheme from "vitepress/theme";
import { h, nextTick, watch } from "vue";

import LhmThemeExtension from "./LhmThemeExtension.vue";

import "./style.css";

export default <Theme>{
  extends: DefaultTheme,
  Layout: () => {
    const { isDark } = useData();
    const initMermaid = () => {
      const mermaidRenderer = createMermaidRenderer({
        theme: isDark.value ? "dark" : "forest",
      });
      mermaidRenderer.setToolbar({
        showLanguageLabel: false,
        desktop: {
          copyCode: "disabled",
        },
        mobile: {
          copyCode: "disabled",
        },
      });
    };
    nextTick(() => initMermaid());
    watch(
      () => isDark.value,
      () => {
        initMermaid();
      }
    );
    return h(LhmThemeExtension);
  },
};
