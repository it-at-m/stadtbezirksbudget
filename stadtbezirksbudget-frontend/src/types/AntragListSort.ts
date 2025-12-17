import type { DataTableSortItem } from "vuetify";

import { sortDefinitions } from "@/types/AntragListSortDefinitions.ts";

const DEFAULT_SORT_KEY: keyof typeof sortDefinitions = "eingangDatum";
const DEFAULT_SORT_DIRECTION: sortDirection = "desc";

// Runtime definition of sorting directions and derived type
export const SORT_DIRECTIONS = ["asc", "desc"] as const;

// Type definition of sortDirection
export type sortDirection = (typeof SORT_DIRECTIONS)[number];

// Interface for Antrag list sorting options
export interface AntragListSortOption {
  title: string;
  sortBy: keyof typeof sortDefinitions;
  sortDirection: sortDirection;
}

//Type definition of AntragListSort based on {@link sortDefinitions}
export type AntragListSort = Record<
  keyof typeof sortDefinitions,
  AntragListSortOption | undefined
>;

// Creates an empty AntragListSort object
export const createEmptyListSort = (): AntragListSort => {
  return Object.keys(sortDefinitions).reduce((acc, key) => {
    acc[key as keyof typeof sortDefinitions] = undefined;
    return acc;
  }, {} as AntragListSort);
};

/**
 * Creates the default AntragListSort object with predefined sorting
 * @returns default {@link AntragListSort}
 * @returns {@link createEmptyListSort} if default sort option is not found
 */
export function createDefaultListSort(): AntragListSort {
  const option = sortDefinitions[DEFAULT_SORT_KEY]?.options.find(
    (o) => o.sortDirection === DEFAULT_SORT_DIRECTION
  );

  if (!option) {
    return createEmptyListSort();
  }

  return {
    ...createEmptyListSort(),
    [DEFAULT_SORT_KEY]: { ...option, sortBy: DEFAULT_SORT_KEY },
  };
}

/**
 * Populates the {@link sortDefinitions} with the corresponding key as sortBy param
 * @param field key for {@link AntragListSortOption}
 * @returns array of {@link AntragListSortOption} for specified field
 */
export const sortOptionsByField = (
  field: keyof typeof sortDefinitions
): AntragListSortOption[] =>
  sortDefinitions[field]?.options.map((value) => ({
    ...value,
    sortBy: field,
  })) ?? [];

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

  const key = firstItem.key as keyof typeof sortDefinitions;
  const match = sortDefinitions[key]?.options.find(
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
