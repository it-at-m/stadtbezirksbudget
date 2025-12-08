import type { AntragListSortOption } from "@/types/AntragListSort.ts";

// Defines which sorting options are available for different values
export const sortOptionsRecord: Record<
  string,
  Omit<AntragListSortOption, "sortBy">[]
> = {
  status: [
    {
      title: "A-Z / Aufsteigend",
      sortDirection: "asc",
    },
    {
      title: "Z-A / Absteigend",
      sortDirection: "desc",
    },
  ],
  zammadNr: [
    {
      title: "Aufsteigend",
      sortDirection: "asc",
    },
    {
      title: "Absteigend",
      sortDirection: "desc",
    },
  ],
  aktenzeichen: [
    {
      title: "Aufsteigend",
      sortDirection: "asc",
    },
    {
      title: "Absteigend",
      sortDirection: "desc",
    },
  ],
  bezirksausschussNr: [
    {
      title: "Aufsteigend",
      sortDirection: "asc",
    },
    {
      title: "Absteigend",
      sortDirection: "desc",
    },
  ],
  eingangDatum: [
    {
      title: "Zuerst hinzugefügt / Aufsteigend",
      sortDirection: "asc",
    },
    {
      title: "Zuletzt hinzugefügt / Absteigend",
      sortDirection: "desc",
    },
  ],
  antragstellerName: [
    {
      title: "A-Z / Aufsteigend",
      sortDirection: "asc",
    },
    {
      title: "Z-A / Absteigend",
      sortDirection: "desc",
    },
  ],
  projektTitel: [
    {
      title: "A-Z / Aufsteigend",
      sortDirection: "asc",
    },
    {
      title: "Z-A / Absteigend",
      sortDirection: "desc",
    },
  ],
  beantragtesBudget: [
    {
      title: "Aufsteigend",
      sortDirection: "asc",
    },
    {
      title: "Absteigend",
      sortDirection: "desc",
    },
  ],
  istFehlbetrag: [
    {
      title: "A-Z / Aufsteigend",
      sortDirection: "asc",
    },
    {
      title: "Z-A / Absteigend",
      sortDirection: "desc",
    },
  ],
  aktualisierung: [
    {
      title: "A-Z / Aufsteigend",
      sortDirection: "asc",
    },
    {
      title: "Z-A / Absteigend",
      sortDirection: "desc",
    },
  ],
  aktualisierungDatum: [
    {
      title: "Zuerst aktualisiert / Aufsteigend",
      sortDirection: "asc",
    },
    {
      title: "Zuletzt aktualisiert / Absteigend",
      sortDirection: "desc",
    },
  ],
};
