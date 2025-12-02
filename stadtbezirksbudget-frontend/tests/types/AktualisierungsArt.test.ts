import type { AktualisierungArtOption } from "@/types/AktualisierungArt";

import { describe, expect, test } from "vitest";

import {
  aktualisierungArtOptions,
  AktualisierungArtText,
} from "@/types/AktualisierungArt.ts";

describe("AktualisierungArt type", () => {
  test("AktualisierungArtText maps each option value to its title", () => {
    aktualisierungArtOptions.forEach((opt: AktualisierungArtOption) => {
      expect(
        AktualisierungArtText[opt.value as keyof typeof AktualisierungArtText]
      ).toBe(opt.title);
    });
  });

  test("aktualisierungArtOptions values are unique", () => {
    const values = aktualisierungArtOptions.map(
      (o: AktualisierungArtOption) => o.value
    );
    expect(new Set(values).size).toBe(values.length);
  });
});
