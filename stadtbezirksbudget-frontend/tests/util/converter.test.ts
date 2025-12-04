import { describe, expect, test } from "vitest";

import { AntragListFilter } from "@/types/AntragListFilter.ts";
import {
  antragListFilterToDTO,
  objectToSearchParams,
} from "@/util/converter.ts";
import { toLocalISOString } from "@/util/formatter";

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

  describe("antragListFilterToDTO", () => {
    const testFilters: AntragListFilter = {
      status: ["EINGEGANGEN", "ABGESCHLOSSEN"],
      bezirksausschussNr: [1, 5],
      eingangDatum: [
        new Date("2025-11-26T00:00:00Z"),
        new Date("2025-11-27T00:00:00Z"),
        new Date("2025-11-28T00:00:00Z"),
      ],
      antragstellerName: "TEST_NAME",
      projektTitel: "TEST_TITEL",
      beantragtesBudgetVon: 537.25,
      beantragtesBudgetBis: 1098.98,
      art: "Fest",
      aktualisierungArt: ["E_AKTE"],
      aktualisierungDatum: [
        new Date("2025-11-24T00:00:00Z"),
        new Date("2025-11-25T00:00:00Z"),
      ],
    };

    test("keeps unchanged fields", () => {
      const dto = antragListFilterToDTO(testFilters);

      expect(dto.status).toBe(testFilters.status);
      expect(dto.bezirksausschussNr).toBe(testFilters.bezirksausschussNr);
      expect(dto.antragstellerName).toBe(testFilters.antragstellerName);
      expect(dto.projektTitel).toBe(testFilters.projektTitel);
      expect(dto.beantragtesBudgetVon).toBe(testFilters.beantragtesBudgetVon);
      expect(dto.beantragtesBudgetBis).toBe(testFilters.beantragtesBudgetBis);
      expect(dto.aktualisierungArt).toBe(testFilters.aktualisierungArt);
    });

    test("converts eingangsDatum array to von and bis iso strings", () => {
      const dto = antragListFilterToDTO(testFilters);

      expect(dto.eingangDatumVon).toBe(
        toLocalISOString(new Date("2025-11-26T00:00:00Z"))
      );
      expect(dto.eingangDatumBis).toBe(
        toLocalISOString(new Date("2025-11-28T00:00:00Z"))
      );
      expect(dto.eingangDatum).toBeUndefined();
    });

    test("converts aktualisierungDatum array to von and bis iso strings", () => {
      const dto = antragListFilterToDTO(testFilters);

      expect(dto.aktualisierungDatumVon).toBe(
        toLocalISOString(new Date("2025-11-24T00:00:00Z"))
      );
      expect(dto.aktualisierungDatumBis).toBe(
        toLocalISOString(new Date("2025-11-25T00:00:00Z"))
      );
      expect(dto.aktualisierungDatum).toBeUndefined();
    });

    test("converts empty date array to undefined von and bis", () => {
      const filters: AntragListFilter = {
        ...testFilters,
        eingangDatum: [],
        aktualisierungDatum: [],
      };
      const dto = antragListFilterToDTO(filters);

      expect(dto.eingangDatumVon).toBeUndefined();
      expect(dto.eingangDatumBis).toBeUndefined();
      expect(dto.aktualisierungDatumVon).toBeUndefined();
      expect(dto.aktualisierungDatumBis).toBeUndefined();
    });

    test("converts art to istFehlbetrag", () => {
      const filters: AntragListFilter = {
        ...testFilters,
      };

      filters.art = "Fest";
      expect(antragListFilterToDTO(filters).istFehlbetrag).toBe(false);

      filters.art = "Fehl";
      expect(antragListFilterToDTO(filters).istFehlbetrag).toBe(true);

      filters.art = undefined;
      expect(antragListFilterToDTO(filters).istFehlbetrag).toBeUndefined();
    });
  });
});
