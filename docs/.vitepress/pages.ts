export type Page = PageItem | PageGroup;

export interface PageItem {
  text: string;
  link: string;
}

export interface PageGroup {
  text: string;
  items: PageItem[];
}

const pages: Page[] = [
  {
    text: "About",
    items: [
      { text: "General", link: "/about/" },
      { text: "Status values", link: "/about/status-values" },
      { text: "Target process", link: "/about/target-process" },
      { text: "Dataflow diagram", link: "/about/dataflow-diagram" },
    ],
  },
  {
    text: "Features",
    items: [
      {
        text: "Application list",
        link: "/features/application-list/",
      },
      {
        text: "Zammad integration",
        link: "/features/zammad-integration",
      },
      {
        text: "CIT integration",
        link: "/features/cit-integration",
      },
      {
        text: "Reliable communication",
        link: "/features/reliable-communication",
      },
    ],
  },
  {
    text: "Architecture",
    items: [
      { text: "Architecture Description", link: "/architecture/" },
      {
        text: "Information Architecture",
        link: "/architecture/information-architecture",
      },
      { text: "Architecture Decision Records", link: "/architecture/adr" },
    ],
  },
  {
    text: "Development",
    items: [
      { text: "Data model", link: "/development/data-model/" },
      { text: "Conventions", link: "/development/conventions" },
      { text: "Tools", link: "/development/tools" },
      { text: "Projects Workflow", link: "/development/projects-workflow" },
      { text: "Authorization", link: "/development/authorization" },
      {
        text: "Database Migration",
        link: "/development/database-migration",
      },
      {
        text: "API Mocking",
        link: "/development/api-mocking",
      },
    ],
  },
];

export default pages;
