import type { AntragListSortOption } from "@/types/AntragListSort.ts";

// Defines which sorting options are available for different values
export const sortDefinitions = {
  status: {
    label: "Status",
    dataTest: "antrag-list-sort-status",
    options: [
      {
        title: "A-Z / Aufsteigend",
        sortDirection: "asc",
      },
      {
        title: "Z-A / Absteigend",
        sortDirection: "desc",
      },
    ],
  },
  zammadNr: {
    label: "Nummer",
    dataTest: "antrag-list-sort-zammad-nr",
    options: [
      {
        title: "Aufsteigend",
        sortDirection: "asc",
      },
      {
        title: "Absteigend",
        sortDirection: "desc",
      },
    ],
  },
  aktenzeichen: {
    label: "Aktenzeichen",
    dataTest: "antrag-list-sort-aktenzeichen",
    options: [
      {
        title: "Aufsteigend",
        sortDirection: "asc",
      },
      {
        title: "Absteigend",
        sortDirection: "desc",
      },
    ],
  },
  bezirksausschussNr: {
    label: "Bezirk",
    dataTest: "antrag-list-sort-bezirksausschuss-nr",
    options: [
      {
        title: "Aufsteigend",
        sortDirection: "asc",
      },
      {
        title: "Absteigend",
        sortDirection: "desc",
      },
    ],
  },
  eingangDatum: {
    label: "Antragsdatum",
    dataTest: "antrag-list-sort-eingang-datum",
    options: [
      {
        title: "Zuerst hinzugefügt / Aufsteigend",
        sortDirection: "asc",
      },
      {
        title: "Zuletzt hinzugefügt / Absteigend",
        sortDirection: "desc",
      },
    ],
  },
  antragstellerName: {
    label: "Antragsteller/in",
    dataTest: "antrag-list-sort-antragsteller-name",
    options: [
      {
        title: "A-Z / Aufsteigend",
        sortDirection: "asc",
      },
      {
        title: "Z-A / Absteigend",
        sortDirection: "desc",
      },
    ],
  },
  projektTitel: {
    label: "Projekt",
    dataTest: "antrag-list-sort-projekt-titel",
    options: [
      {
        title: "A-Z / Aufsteigend",
        sortDirection: "asc",
      },
      {
        title: "Z-A / Absteigend",
        sortDirection: "desc",
      },
    ],
  },
  beantragtesBudget: {
    label: "Beantragtes Budget",
    dataTest: "antrag-list-sort-beantragtes-budget",
    options: [
      {
        title: "Aufsteigend",
        sortDirection: "asc",
      },
      {
        title: "Absteigend",
        sortDirection: "desc",
      },
    ],
  },
  finanzierungArt: {
    label: "Art",
    dataTest: "antrag-list-sort-finanzierung-art",
    options: [
      {
        title: "A-Z / Aufsteigend",
        sortDirection: "asc",
      },
      {
        title: "Z-A / Absteigend",
        sortDirection: "desc",
      },
    ],
  },
  aktualisierung: {
    label: "Aktualisierung",
    dataTest: "antrag-list-sort-aktualisierung",
    options: [
      {
        title: "A-Z / Aufsteigend",
        sortDirection: "asc",
      },
      {
        title: "Z-A / Absteigend",
        sortDirection: "desc",
      },
    ],
  },
  aktualisierungDatum: {
    label: "Datum Aktualisierung",
    dataTest: "antrag-list-sort-aktualisierung-datum",
    options: [
      {
        title: "Zuerst aktualisiert / Aufsteigend",
        sortDirection: "asc",
      },
      {
        title: "Zuletzt aktualisiert / Absteigend",
        sortDirection: "desc",
      },
    ],
  },
} satisfies Record<
  string,
  {
    label: string;
    dataTest: string;
    options: Omit<AntragListSortOption, "sortBy">[];
  }
>;
