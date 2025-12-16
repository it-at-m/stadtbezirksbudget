import type { FinanzierungArtOption } from "@/types/FinanzierungArt";

import { describe, expect, test } from "vitest";

import {
  finanzierungArtOptions,
  FinanzierungArtText,
} from "@/types/FinanzierungArt.ts";

describe("FinanzierungArt", () => {
  test("FinanzierungArtText maps each option value to its title", () => {
    finanzierungArtOptions.forEach((opt: FinanzierungArtOption) => {
      expect(
        FinanzierungArtText[opt.value as keyof typeof FinanzierungArtText]
      ).toBe(opt.title);
    });
  });

  test("finanzierungArtOptions values are unique", () => {
    const values = finanzierungArtOptions.map(
      (o: FinanzierungArtOption) => o.value
    );
    expect(new Set(values).size).toBe(values.length);
  });
});
