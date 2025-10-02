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
          { text: "Architecture Design Records", link: "/architecture/adr" },
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
    sidebar: {
      "/": [
        { text: "About", link: "/about/" },
        {
          text: "Features",
          link: "/features/",
          items: [],
        },
        {
          text: "Architecture",
          items: [
            { text: "Architecture Design Records", link: "/architecture/adr" },
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
      "/architecture/adr/": [
        {
          text: "Architecture Design Records",
          items: [
            {
              text: "Overview",
              link: "/architecture/adr",
            },
            {
              text: "ADR-00 Is-Template",
              link: "/architecture/adr/00-is-template",
            },
            {
              text: "ADR-01 Use-Immutable-Variables",
              link: "/architecture/adr/01-use-immutable-variables",
            },
            {
              text: "ADR-02 No-Column-Annotation",
              link: "/architecture/adr/02-no-column-annotation",
            },
            {
              text: "ADR-03 Use-Test-File-Suffix",
              link: "/architecture/adr/03-use-test-file-suffix",
            },
            {
              text: "ADR-04 Use-Test-Nested-Class",
              link: "/architecture/adr/04-use-test-nested-class",
            },
            {
              text: "ADR-05 Use-Test-Method-Prefix",
              link: "/architecture/adr/05-use-test-method-prefix",
            },
            {
              text: "ADR-06 Use-Test-Data-Attribute",
              link: "/architecture/adr/06-use-test-data-attribute",
            },
            {
              text: "ADR-07 Reduce-Testing-Of-Mapping",
              link: "/architecture/adr/07-reduce-testing-of-mapping",
            },
            {
              text: "ADR-08 Define-Dtos-As-Records",
              link: "/architecture/adr/08-define-dtos-as-records",
            },
            {
              text: "ADR-09 No-Complex-Frontend-Components",
              link: "/architecture/adr/09-no-complex-frontend-components",
            },
            {
              text: "ADR-10 Send-Api-Error-Message",
              link: "/architecture/adr/10-send-api-error-message",
            },
            {
              text: "ADR-11 Use-Directory-Structure",
              link: "/architecture/adr/11-use-directory-structure",
            },
            {
              text: "ADR-12 Use-Comments",
              link: "/architecture/adr/12-use-comments",
            },
          ],
        },
      ],
    },
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
