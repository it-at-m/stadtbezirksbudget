import { describe, expect, test } from "vitest";

import { objectToSearchParams, routeParamsToString } from "@/util/converter.ts";

describe("converter", () => {
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

      const result = objectToSearchParams(object, params);
      expect(result).toBe(params);
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

  describe("routeParamsToString", () => {
    test("returns the string itself when param is a string", () => {
      const result = routeParamsToString("hello");
      expect(result).toBe("hello");
    });

    test("returns the first string from the array when param is an array", () => {
      const result = routeParamsToString(["hello", "world"]);
      expect(result).toBe("hello");
    });

    test("returns an empty string when param is an empty array", () => {
      const result = routeParamsToString([]);
      expect(result).toBe("");
    });

    test("returns an empty string when param is undefined", () => {
      const result = routeParamsToString(undefined);
      expect(result).toBe("");
    });

    test("returns an empty string when param is an array with undefined", () => {
      const result = routeParamsToString([undefined, "world"]);
      expect(result).toBe("");
    });
  });
});
