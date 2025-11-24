import { defineConfig } from "vitepress";
import lightbox from "vitepress-plugin-lightbox";
import { withMermaid } from "vitepress-plugin-mermaid";

import navigation from "./navigation";
import sidebar from "./sidebar";

// https://vitepress.dev/reference/site-config
const vitepressConfig = defineConfig({
  base: "/stadtbezirksbudget/",
  title: "Stadtbezirksbudget",
  description: "Documentation for Stadtbezirksbudget",
  head: [
    [
      "link",
      {
        rel: "icon",
        href: `https://assets.muenchen.de/logos/lhm/icon-lhm-muenchen-32.png`,
      },
    ],
  ],
  themeConfig: {
    // https://vitepress.dev/reference/default-theme-config
    nav: navigation,
    sidebar: sidebar,
    socialLinks: [
      { icon: "github", link: "https://github.com/it-at-m/stadtbezirksbudget" },
    ],
    editLink: {
      pattern:
        "https://github.com/it-at-m/stadtbezirksbudget/blob/main/docs/src/:path",
      text: "View this page on GitHub",
    },
    footer: {
      message: `<a href="https://opensource.muenchen.de/impress.html">Impress and Contact</a>`,
    },
    outline: {
      level: "deep",
    },
    search: {
      provider: "local",
    },
  },
  markdown: {
    config: (md) => {
      md.use(lightbox, {});
    },
    image: {
      lazyLoading: true,
    },
  },
  srcDir: "./src",
  cleanUrls: true,
  lastUpdated: true,
  vite: {
    publicDir: "../public",
  },
});

export default withMermaid(vitepressConfig);
