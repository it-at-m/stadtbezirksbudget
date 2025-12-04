import type { BezirksausschussNrOption } from "@/types/BezirksausschussNr";

import { describe, expect, test } from "vitest";

import { bezirksausschussNrOptions } from "@/types/BezirksausschussNr.ts";

describe("BezirksausschussNr", () => {
  describe("bezirksausschussNrOptions", () => {
    test("has 25 entries and values 1..25", () => {
      expect(bezirksausschussNrOptions).toHaveLength(25);
      const values = bezirksausschussNrOptions.map(
        (o: BezirksausschussNrOption) => o.value
      );
      expect(values).toEqual(Array.from({ length: 25 }, (_, i) => i + 1));
    });

    test("titles are `Bezirk N` and order is correct", () => {
      bezirksausschussNrOptions.forEach(
        (opt: BezirksausschussNrOption, i: number) => {
          expect(typeof opt.title).toBe("string");
          expect(opt.title).toBe(`Bezirk ${i + 1}`);
          expect(opt.value).toBe(i + 1);
        }
      );
    });

    test("values are unique", () => {
      const values = bezirksausschussNrOptions.map(
        (o: BezirksausschussNrOption) => o.value
      );
      const unique = new Set(values);
      expect(unique.size).toBe(values.length);
    });
  });
});
