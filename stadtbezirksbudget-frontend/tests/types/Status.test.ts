import type { StatusOption } from "@/types/Status.ts";

import { describe, expect, test } from "vitest";

import { statusOptions, StatusText } from "@/types/Status.ts";

describe("Status", () => {
  test("StatusText maps each option value to its short and long text", () => {
    statusOptions.forEach((opt: StatusOption) => {
      expect(StatusText[opt.value as keyof typeof StatusOption]).toStrictEqual({
        shortText: opt.shortText,
        longText: opt.longText,
      });
    });
  });

  test("statusOptions values are unique", () => {
    const values = statusOptions.map((o: StatusOption) => o.value);
    expect(new Set(values).size).toBe(values.length);
  });
});
