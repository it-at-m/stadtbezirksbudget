import { describe, expect, test } from "vitest";

import { objectToSearchParams } from "@/util/converter.ts";

describe("converter util", () => {
  describe("objectToSearchParams", () => {
    test("convert empty object", () => {
      const object = {};
      const params = new URLSearchParams();

      objectToSearchParams(object, params);
      expect(params).toStrictEqual(params);
      expect(params.toString()).toBe("");
    });

    test("skip empty values", () => {
      const object = {
        undefined: undefined,
        null: null,
        emptyString: "",
      };
      const params = new URLSearchParams();

      objectToSearchParams(object, params);
      expect(params).toStrictEqual(params);
      expect(params.toString()).toBe("");
    });

    test("appends primitive values", () => {
      const object = {
        string: "string",
        number: 42,
        boolean: true,
      };
      const params = new URLSearchParams({
        existing: "existing",
      });

      objectToSearchParams(object, params);
      expect(params.toString()).toBe(
        "existing=existing&string=string&number=42&boolean=true"
      );
    });

    test("appends array values", () => {
      const object = {
        array: [1, 2, 3],
      };
      const params = new URLSearchParams({
        existing: "existing",
      });

      objectToSearchParams(object, params);
      expect(params.toString()).toBe("existing=existing&array=1%2C2%2C3");
    });

    test("skips empty arrays", () => {
      const object = {
        array: [],
      };
      const params = new URLSearchParams();

      objectToSearchParams(object, params);
      expect(params).toStrictEqual(params);
      expect(params.toString()).toBe("");
    });

    test("creates new URLSearchParams", () => {
      const params = objectToSearchParams({});
      expect(params).toBeInstanceOf(URLSearchParams);
    });
  });
});
