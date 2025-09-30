import { defineConfig } from "vitepress";
import { withMermaid } from "vitepress-plugin-mermaid";

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
    nav: [
      { text: "Home", link: "/" },
      { text: "About", link: "/about/" },
      {
        text: "Features",
        items: [{ text: "Features", link: "/features/" }],
      },
      {
        text: "Architecture",
        items: [
          { text: "Architecture Design Records", link: "/architecture/adr/" },
        ],
      },
      {
        text: "Development",
        items: [
          { text: "Tools", link: "/development/tools" },
          {
            text: "Database Migration",
            link: "/development/database-migration",
          },
        ],
      },
    ],
    sidebar: [
      { text: "About", link: "/about/" },
      {
        text: "Features",
        link: "/features/",
        items: [],
      },
      {
        text: "Architecture",
        items: [
          { text: "Architecture Design Records", link: "/architecture/adr/" },
        ],
      },
      {
        text: "Development",
        items: [
          { text: "Tools", link: "/development/tools" },
          {
            text: "Database Migration",
            link: "/development/database-migration",
          },
        ],
      },
    ],
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
