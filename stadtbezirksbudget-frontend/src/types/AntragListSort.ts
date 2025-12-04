// Type definition of sorting diretion
import type { sortOptionsRecord } from "@/types/AntragListSortDefinitions.ts";

import { sortOptionsRecord as sortOptionDefinitions } from "@/types/AntragListSortDefinitions.ts";

export type sortDirection = "asc" | "desc";

// Interface for Antrag list sorting options
export interface AntragListSortOption {
  title: string;
  sortBy: string;
  sortDirection: sortDirection;
}

// Type definition of AntragListSort based of {@link sortOptionsRecord}
export type AntragListSort = Record<string, AntragListSortOption | undefined>;

// Creates an empty AntragListSort object
export const createEmptyListSort = (): AntragListSort => {
  return Object.keys(sortOptionDefinitions).reduce((acc, key) => {
    acc[key] = undefined;
    return acc;
  }, {} as AntragListSort);
};

// Populates the {@link sortOptionsRecord} with the corresponding key as sortBy param
export const sortOptionsByField = (
  field: keyof typeof sortOptionsRecord
): AntragListSortOption[] =>
  sortOptionDefinitions[field]?.map((value) => ({ ...value, sortBy: field })) ||
  [];

// Converts AntragListSort to backend sort string format
export const antragListSortToSortString = (sort: AntragListSort): string => {
  const sortStrings = Object.values(sort)
    .filter((v): v is AntragListSortOption => v !== undefined)
    .map((v) => {
      const mapped = antragSortFieldMapper[v.sortBy];
      return mapped
        ? `${mapped},${v.sortDirection}`
        : `${v.sortBy},${v.sortDirection}`;
    });

  return sortStrings.join(";");
};

const antragSortFieldMapper: Record<string, string> = {
  status: "bearbeitungsstand.status",
  zammadNr: "zammadNr",
  aktenzeichen: "aktenzeichen",
  bezirksausschussNr: "bezirksausschussNr",
  eingangDatum: "eingangDatum",
  antragstellerName: "antragsteller.name",
};
