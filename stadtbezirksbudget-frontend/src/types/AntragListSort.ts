import type { DataTableSortItem } from "vuetify";

import { sortOptionsRecord } from "@/types/AntragListSortDefinitions.ts";

// Runtime definition of sorting directions and derived type
export const SORT_DIRECTIONS = ["asc", "desc"] as const;

// Type definition of sortDirection
export type sortDirection = (typeof SORT_DIRECTIONS)[number];

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
  return Object.keys(sortOptionsRecord).reduce((acc, key) => {
    acc[key as keyof typeof sortOptionsRecord] = undefined;
    return acc;
  }, {} as AntragListSort);
};

/**
 * Populates the {@link sortOptionsRecord} with the corresponding key as sortBy param
 * @param field key for {@link AntragListSortOption}
 * @returns array of {@link AntragListSortOption} for specified field
 */
export const sortOptionsByField = (
  field: keyof typeof sortOptionsRecord
): AntragListSortOption[] =>
  sortOptionsRecord[field]?.map((value) => ({ ...value, sortBy: field })) ?? [];

/**
 * Converts AntragListSort to backend sort string format
 * @param sort the {@link AntragListSort} to convert
 * @return sorting dto
 */
export const antragListSortToSortDto = (
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
 * Converts an array of DataTableSortItem to AntragListSortOption
 * @param sortItems - The DataTableSortItem array to convert
 * @returns The corresponding AntragListSortOption or undefined if not found
 */
export function antragListSortOptionFromSortItems(
  sortItems: DataTableSortItem[]
): AntragListSortOption | undefined {
  const firstItem = sortItems[0];
  if (
    !firstItem ||
    !SORT_DIRECTIONS.includes(String(firstItem.order) as sortDirection)
  )
    return;

  const key = firstItem.key as keyof typeof sortOptionsRecord;
  const match = sortOptionsRecord[key]?.find(
    (o) => o.sortDirection === firstItem.order
  );
  return match
    ? {
        ...match,
        sortBy: key,
        sortDirection: firstItem.order as sortDirection,
      }
    : undefined;
}

/**
 * Converts AntragListSort to DataTableSortItem array for Vuetify data table sorting
 * @param sort the {@link AntragListSort} to convert
 * @returns array of {@link DataTableSortItem}
 */
export const antragListSortToSortItem = (
  sort: AntragListSort
): DataTableSortItem[] =>
  Object.values(sort)
    .filter((v): v is AntragListSortOption => v !== undefined)
    .map((v) => ({
      key: v.sortBy,
      order: v.sortDirection,
    }));
