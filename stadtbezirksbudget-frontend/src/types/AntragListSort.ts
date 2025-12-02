export type sortDirection = "asc" | "desc";

export interface AntragListSort {
  title: string;
  sortBy: string;
  sortDirection: sortDirection;
}

export const sortOptions: Record<string, AntragListSort[]> = {
  status: [
    {
      title: "A-Z / Aufsteigend",
      sortBy: "status",
      sortDirection: "asc",
    },
    {
      title: "Z-A / Absteigend",
      sortBy: "status",
      sortDirection: "desc",
    },
  ],
  nummer: [
    {
      title: "Aufsteigend",
      sortBy: "nummer",
      sortDirection: "asc",
    },
    {
      title: "Absteigend",
      sortBy: "nummer",
      sortDirection: "desc",
    },
  ],
  aktenzeichen: [
    {
      title: "Aufsteigend",
      sortBy: "aktenzeichen",
      sortDirection: "asc",
    },
    {
      title: "Absteigend",
      sortBy: "aktenzeichen",
      sortDirection: "desc",
    },
  ],
  bezirk: [
    {
      title: "Aufsteigend",
      sortBy: "bezirk",
      sortDirection: "asc",
    },
    {
      title: "Absteigend",
      sortBy: "bezirk",
      sortDirection: "desc",
    },
  ],
  antragsdatum: [
    {
      title: "Zuerst hinzugefügt / Aufsteigend",
      sortBy: "antragsdatum",
      sortDirection: "asc",
    },
    {
      title: "Zuletzt hinzugefügt / Absteigend",
      sortBy: "antragsdatum",
      sortDirection: "desc",
    },
  ],
  antragsteller: [
    {
      title: "A-Z / Aufsteigend",
      sortBy: "antragsteller",
      sortDirection: "asc",
    },
    {
      title: "Z-A / Absteigend",
      sortBy: "antragsteller",
      sortDirection: "desc",
    },
  ],
  projekt: [
    {
      title: "A-Z / Aufsteigend",
      sortBy: "projekt",
      sortDirection: "asc",
    },
    {
      title: "Z-A / Absteigend",
      sortBy: "projekt",
      sortDirection: "desc",
    },
  ],
  beantragtesBudget: [
    {
      title: "Aufsteigend",
      sortBy: "beantragtesBudget",
      sortDirection: "asc",
    },
    {
      title: "Absteigend",
      sortBy: "beantragtesBudget",
      sortDirection: "desc",
    },
  ],
  art: [
    {
      title: "A-Z / Aufsteigend",
      sortBy: "art",
      sortDirection: "asc",
    },
    {
      title: "Z-A / Absteigend",
      sortBy: "art",
      sortDirection: "desc",
    },
  ],
  aktualisierungArt: [
    {
      title: "A-Z / Aufsteigend",
      sortBy: "aktualisierungArt",
      sortDirection: "asc",
    },
    {
      title: "Z-A / Absteigend",
      sortBy: "aktualisierungArt",
      sortDirection: "desc",
    },
  ],
  aktualisierungDatum: [
    {
      title: "Zuerst aktualisiert / Aufsteigend",
      sortBy: "aktualisierungDatum",
      sortDirection: "asc",
    },
    {
      title: "Zuletzt aktualisiert / Absteigend",
      sortBy: "aktualisierungDatum",
      sortDirection: "desc",
    },
  ],
};
