import type { sortOptionsRecord } from "@/types/AntragListSortDefinitions.ts";
import type { DataTableSortItem } from "vuetify";

import { sortOptionsRecord as sortOptionDefinitions } from "@/types/AntragListSortDefinitions.ts";

// Type definition of sorting direction
export type sortDirection = "asc" | "desc";

// Interface for Antrag list sorting options
export interface AntragListSortOption {
  title: string;
  sortBy: keyof typeof sortOptionsRecord;
  sortDirection: sortDirection;
}

//Type definition of AntragListSort based on {@link sortOptionsRecord}
export type AntragListSort = Record<
  keyof typeof sortOptionsRecord,
  AntragListSortOption | undefined
>;

// Creates an empty AntragListSort object
export const createEmptyListSort = (): AntragListSort => {
  return Object.keys(sortOptionDefinitions).reduce((acc, key) => {
    acc[key as keyof typeof sortOptionDefinitions] = undefined;
    return acc;
  }, {} as AntragListSort);
};

// Populates the {@link sortOptionsRecord} with the corresponding key as sortBy param
export const sortOptionsByField = (
  field: keyof typeof sortOptionsRecord
): AntragListSortOption[] =>
  sortOptionDefinitions[field]?.map((value) => ({ ...value, sortBy: field })) ??
  [];

// Converts AntragListSort to backend sort string format
export const antragListSortToSortString = (
  sort: AntragListSort
): { sortBy: string; sortDirection: string } => {
  const item = Object.values(sort).find(
    (v): v is AntragListSortOption => v !== undefined
  );

  if (!item) {
    return { sortBy: "", sortDirection: "" };
  }

  return {
    sortBy: item.sortBy,
    sortDirection: item.sortDirection.toUpperCase(),
  };
};

/**
 * Converts DataTableSortItem or array of DataTableSortItem to AntragListSortOption
 * @param sortItems - The DataTableSortItem or array of DataTableSortItem to convert
 * @returns The corresponding AntragListSortOption or undefined if not found
 */
export const antragListSortOptionFromSortItems = (
  sortItems: DataTableSortItem[]
): AntragListSortOption | undefined => {
  const i = sortItems[0];
  if (!i || !["asc", "desc"].includes(String(i.order))) return;

  const match = sortOptionDefinitions[i.key]?.find(
    (o) => o.sortDirection === i.order
  );
  return match
    ? {
        ...match,
        sortBy: i.key,
        sortDirection: i.order as sortDirection,
      }
    : undefined;
};

// Converts AntragListSort to DataTableSortItem array for Vuetify data table sorting
export const antragListSortToSortItem = (
  sort: AntragListSort
): DataTableSortItem[] => {
  return Object.values(sort)
    .filter((v): v is AntragListSortOption => v !== undefined)
    .map((v) => ({
      key: v.sortBy,
      order: v.sortDirection,
    }));
};

/**
 * Converts AntragListSort to URLSearchParams.
 * @param sortObject - The AntragListSort object to convert
 * @param params - Optional existing URLSearchParams to append to
 * @returns The resulting URLSearchParams
 */
export function sortObjectToSearchParams(
  sortObject: AntragListSort,
  params: URLSearchParams = new URLSearchParams()
): URLSearchParams {
  const sortString = antragListSortToSortString(sortObject);
  if (sortString.sortBy && sortString.sortDirection) {
    params.append("sortBy", sortString.sortBy);
    params.append("sortDirection", sortString.sortDirection);
  }
  return params;
}
