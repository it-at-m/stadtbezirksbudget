import { describe, expect, test } from "vitest";
import { DataTableSortItem } from "vuetify/framework";

import {
  antragListSortOptionFromSortItems,
  antragListSortToSortDto,
  antragListSortToSortItem,
  createEmptyListSort,
  sortOptionsByField,
} from "../../src/types/AntragListSort";
import { sortOptionsRecord } from "../../src/types/AntragListSortDefinitions";

describe("AntragListSort", () => {
  test("create empty AntragListSort returns empty AntragListSort", () => {
    const emptySort = createEmptyListSort();
    expect(Object.values(emptySort).every((v) => v === undefined)).toBe(true);
  });

  describe("sortOptionsByField", () => {
    const fields = Object.keys(
      sortOptionsRecord
    ) as (keyof typeof sortOptionsRecord)[];

    const optionCases: [
      keyof typeof sortOptionsRecord,
      { title: string; sortDirection: string },
    ][] = [];

    const countCases: [keyof typeof sortOptionsRecord, number][] = [];

    for (const field of fields) {
      const defs = sortOptionsRecord[field] || [];
      countCases.push([field, defs.length]);
      for (const def of defs) {
        optionCases.push([
          field,
          { title: def.title, sortDirection: def.sortDirection },
        ]);
      }
    }

    test.each(optionCases)(
      "returns correct sort option for field '%s' with expected %o",
      (field, expected) => {
        const opts = sortOptionsByField(field);
        const found = opts.find(
          (o) => o.sortDirection === expected.sortDirection
        );
        expect(found).toBeDefined();
        if (found) {
          expect(found.title).toBe(expected.title);
          expect(found.sortBy).toBe(field);
          expect(found.sortDirection).toBe(expected.sortDirection);
        }
      }
    );

    test.each(countCases)(
      "returns %d options for field '%s'",
      (field, expectedLength) => {
        const opts = sortOptionsByField(field);
        expect(opts.length).toBe(expectedLength);
      }
    );
    test("returns empty array for invalid field", () => {
      const opts = sortOptionsByField("invalid_field" as never);
      expect(opts).toEqual([]);
    });
  });

  describe("antragListSortToSortString", () => {
    test("converts AntragListSort to correct sort string", () => {
      const sort = createEmptyListSort();
      sort["test"] = {
        title: "Test",
        sortBy: "test",
        sortDirection: "asc",
      };

      const result = antragListSortToSortDto(sort);

      expect(result).toStrictEqual({ sortBy: "test", sortDirection: "ASC" });
    });

    test("returns empty strings for empty sort", () => {
      const result = antragListSortToSortDto(createEmptyListSort());
      expect(result).toStrictEqual({ sortBy: "", sortDirection: "" });
    });
  });

  describe("antragListSortOptionFromSortItems", () => {
    test("returns matching option for valid order", () => {
      const key = Object.keys(
        sortOptionsRecord
      )[0] as keyof typeof sortOptionsRecord;
      const def = sortOptionsRecord[key]?.[0];
      const res = antragListSortOptionFromSortItems([
        { key, order: def.sortDirection } as DataTableSortItem,
      ]);
      expect(res).toBeDefined();
      expect(res?.sortBy).toBe(key);
      expect(res?.sortDirection).toBe(def.sortDirection);
    });

    test("returns undefined for empty input", () => {
      expect(antragListSortOptionFromSortItems([])).toBeUndefined();
    });

    test("returns undefined for invalid order", () => {
      const key = Object.keys(
        sortOptionsRecord
      )[0] as keyof typeof sortOptionsRecord;
      expect(
        antragListSortOptionFromSortItems([{ key, order: "invalid" }] as never)
      ).toBeUndefined();
    });

    test("returns undefined for non-matching option", () => {
      const res = antragListSortOptionFromSortItems([
        { key: "non_existent_key", order: "asc" },
      ] as DataTableSortItem[]);
      expect(res).toBeUndefined();
    });
  });

  describe("antragListSortToSortItem", () => {
    test("returns empty array for empty sort", () => {
      const res = antragListSortToSortItem(createEmptyListSort());
      expect(res).toEqual([]);
    });

    test("converts single entry correctly", () => {
      const sort = createEmptyListSort();
      sort["test"] = { title: "Test", sortBy: "test", sortDirection: "asc" };
      const res = antragListSortToSortItem(sort);
      expect(res).toEqual([{ key: "test", order: "asc" }]);
    });
  });
});
