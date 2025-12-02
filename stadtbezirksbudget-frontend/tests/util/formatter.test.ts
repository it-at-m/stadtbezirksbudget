import { describe, expect, test } from "vitest";

import {
  booleanToFestOrFehl,
  toDateString,
  toLocalISOString,
  toNumberString,
  toTimeString,
} from "@/util/formatter.ts";

describe("formatter", () => {
  describe("toDateString", () => {
    test("formats valid date to 'DD.MM.YYYY'", () => {
      const date = new Date("2023-05-15");
      expect(toDateString(date)).toBe("15.05.2023");
    });

    test("invalid date returns empty string", () => {
      const invalidDate = new Date("Invalid Date");
      expect(toDateString(invalidDate)).toBe("");
    });
  });

  describe("toTimeString", () => {
    test("formats valid date to 'HH:mm:ss'", () => {
      const date = new Date("2023-05-15T14:30:00");
      expect(toTimeString(date)).toBe("14:30:00");
    });

    test("invalid date returns empty string", () => {
      const invalidDate = new Date("Invalid Date");
      expect(toTimeString(invalidDate)).toBe("");
    });
  });

  describe("toLocalISOString", () => {
    test("formats valid date to 'YYYY-MM-DDTHH:mm:ss'", () => {
      const date = new Date("2025-11-27T00:00:00");
      expect(toLocalISOString(date)).toBe("2025-11-27T00:00:00");
    });

    test("invalid date returns undefined", () => {
      const invalidDate = new Date("Invalid Date");
      expect(toLocalISOString(invalidDate)).toBeUndefined();
    });
  });

  describe("toNumberString", () => {
    test("formats default fraction digits", () => {
      const amount = 1234.567;
      expect(toNumberString(amount)).toBe("1.234,57");
    });

    test("formats specified fraction digits", () => {
      const amount = 1234.567;
      expect(toNumberString(amount, 1)).toBe("1.234,6");
    });

    test("formats number with currency", () => {
      const amount = 1234.567;
      expect(toNumberString(amount, 2, "EUR")).toBe("1.234,57 €");
    });
  });

  describe("booleanToFestOrFehl", () => {
    test("formats true to 'Fehl'", () => {
      expect(booleanToFestOrFehl(true)).toBe("Fehl");
    });

    test("formats false to 'Fest'", () => {
      expect(booleanToFestOrFehl(false)).toBe("Fest");
    });

    test("formats null to empty string", () => {
      expect(booleanToFestOrFehl(null)).toBe("");
    });

    test("formats undefined to empty string", () => {
      expect(booleanToFestOrFehl(undefined)).toBe("");
    });
  });
});
