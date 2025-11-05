import { describe, expect, test } from "vitest";

import {
  booleanToFestOrFehl,
  toDateString,
  toNumberString,
  toTimeString,
} from "@/util/formatter.ts";

describe("formatter.ts tests", () => {
  describe("toDateString", () => {
    test("testFormatValidDateDeLocale", () => {
      const date = new Date("2023-05-15");
      expect(toDateString(date)).toBe("15.05.2023");
    });

    test("testInvalidDateReturnsEmptyString", () => {
      const invalidDate = new Date("Invalid Date");
      expect(toDateString(invalidDate)).toBe("");
    });
  });

  describe("toTimeString", () => {
    test("testFormatValidDateToTimeString", () => {
      const date = new Date("2023-05-15T14:30:00");
      expect(toTimeString(date)).toBe(date.toLocaleTimeString());
    });

    test("testInvalidDateReturnsEmptyString", () => {
      const invalidDate = new Date("Invalid Date");
      expect(toTimeString(invalidDate)).toBe("");
    });
  });

  describe("toNumberString", () => {
    test("testFormatNumberDefaultFractionDigits", () => {
      const amount = 1234.567;
      expect(toNumberString(amount)).toBe("1.234,57");
    });

    test("testFormatNumberSpecifiedFractionDigits", () => {
      const amount = 1234.567;
      expect(toNumberString(amount, 1)).toBe("1.234,6");
    });

    test("testFormatNumberWithCurrency", () => {
      const amount = 1234.567;
      expect(toNumberString(amount, 2, "EUR")).toBe("1.234,57 €");
    });
  });

  describe("booleanToFestOrFehl", () => {
    test("testBooleanTrueReturnsFehl", () => {
      expect(booleanToFestOrFehl(true)).toBe("Fehl");
    });

    test("testBooleanFalseReturnsFest", () => {
      expect(booleanToFestOrFehl(false)).toBe("Fest");
    });

    test("testNullReturnsEmptyString", () => {
      expect(booleanToFestOrFehl(null)).toBe("");
    });

    test("testUndefinedReturnsEmptyString", () => {
      expect(booleanToFestOrFehl(undefined)).toBe("");
    });
  });
});
