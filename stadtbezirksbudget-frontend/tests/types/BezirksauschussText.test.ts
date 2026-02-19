import type { BezirksausschussOption } from "@/types/BezirksauschussText";

import { describe, expect, test } from "vitest";

import {
  bezirksausschussOptions,
  BezirksausschussText,
  getBezirksnameByNumber,
} from "@/types/BezirksauschussText";

describe("BezirksausschussText", () => {
  test("BezirksausschussText maps each option value to its title", () => {
    bezirksausschussOptions.forEach((opt: BezirksausschussOption) => {
      expect(
        BezirksausschussText[opt.value as keyof typeof BezirksausschussText]
      ).toBe(opt.title);
    });
  });

  test("bezirksausschussOptions values are unique", () => {
    const values = bezirksausschussOptions.map(
      (o: BezirksausschussOption) => o.value
    );
    expect(new Set(values).size).toBe(values.length);
  });

  test("getBezirksnameByNumber returns the correct name for valid numbers", () => {
    bezirksausschussOptions.forEach((option) => {
      expect(getBezirksnameByNumber(option.value)).toBe(option.title);
    });
  });

  test("getBezirksnameByNumber throws an error for invalid numbers", () => {
    const invalidNumbers = [0, 26, -1, 30];

    invalidNumbers.forEach((number) => {
      expect(() => getBezirksnameByNumber(number)).toThrow(
        `Invalid Bezirksausschuss number: ${number}`
      );
    });
  });
});
