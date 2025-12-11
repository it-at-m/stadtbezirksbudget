import type { AntragListFilter } from "@/types/AntragListFilter";

import { describe, expect, test } from "vitest";

import { antragListFilterToDTO } from "@/types/AntragListFilterDTO.ts";
import { toLocalISOString } from "@/util/formatter.ts";

describe("AntragListFilterDTO", () => {
  describe("antragListFilterToDTO", () => {
    const testFilters: AntragListFilter = {
      status: ["EINGEGANGEN", "ABGESCHLOSSEN"],
      bezirksausschussNr: [1, 5],
      eingangDatumVon: new Date("2025-11-26T00:00:00Z"),
      eingangDatumBis: new Date("2025-11-28T00:00:00Z"),
      antragstellerName: "TEST_NAME",
      projektTitel: "TEST_TITEL",
      beantragtesBudgetVon: 537.25,
      beantragtesBudgetBis: 1098.98,
      art: "Fest",
      aktualisierungArt: ["E_AKTE"],
      aktualisierungDatumVon: new Date("2025-11-24T00:00:00Z"),
      aktualisierungDatumBis: new Date("2025-11-25T00:00:00Z"),
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

    test("converts eingangsDatum to iso strings", () => {
      const dto = antragListFilterToDTO(testFilters);

      expect(dto.eingangDatumVon).toBe(
        toLocalISOString(new Date("2025-11-26T00:00:00Z"))
      );
      expect(dto.eingangDatumBis).toBe(
        toLocalISOString(new Date("2025-11-28T23:59:59"))
      );
    });

    test("converts aktualisierungDatum to iso strings", () => {
      const dto = antragListFilterToDTO(testFilters);

      expect(dto.aktualisierungDatumVon).toBe(
        toLocalISOString(new Date("2025-11-24T00:00:00Z"))
      );
      expect(dto.aktualisierungDatumBis).toBe(
        toLocalISOString(new Date("2025-11-25T23:59:59"))
      );
    });

    test("converts partial date ranges future", () => {
      const filters: AntragListFilter = {
        ...testFilters,
        eingangDatumBis: null,
        aktualisierungDatumBis: null,
      };
      const dto = antragListFilterToDTO(filters);

      expect(dto.eingangDatumVon).toBe(
        toLocalISOString(new Date("2025-11-26T00:00:00Z"))
      );
      expect(dto.eingangDatumBis).toBeUndefined();
      expect(dto.aktualisierungDatumVon).toBe(
        toLocalISOString(new Date("2025-11-24T00:00:00Z"))
      );
      expect(dto.aktualisierungDatumBis).toBeUndefined();
    });

    test("converts partial date ranges past", () => {
      const filters: AntragListFilter = {
        ...testFilters,
        eingangDatumVon: null,
        aktualisierungDatumVon: null,
      };
      const dto = antragListFilterToDTO(filters);

      expect(dto.eingangDatumVon).toBeUndefined();
      expect(dto.eingangDatumBis).toBe(
        toLocalISOString(new Date("2025-11-28T23:59:59"))
      );
      expect(dto.aktualisierungDatumVon).toBeUndefined();
      expect(dto.aktualisierungDatumBis).toBe(
        toLocalISOString(new Date("2025-11-25T23:59:59"))
      );
    });

    test("converts null dates to undefined", () => {
      const filters: AntragListFilter = {
        ...testFilters,
        eingangDatumVon: null,
        eingangDatumBis: null,
        aktualisierungDatumVon: null,
        aktualisierungDatumBis: null,
      };
      const dto = antragListFilterToDTO(filters);

      expect(dto.eingangDatumVon).toBeUndefined();
      expect(dto.eingangDatumBis).toBeUndefined();
      expect(dto.aktualisierungDatumVon).toBeUndefined();
      expect(dto.aktualisierungDatumBis).toBeUndefined();
    });

    test("converts art to istFehlbetrag", () => {
      expect(
        antragListFilterToDTO({ ...testFilters, art: "Fest" }).istFehlbetrag
      ).toBe(false);
      expect(
        antragListFilterToDTO({ ...testFilters, art: "Fehl" }).istFehlbetrag
      ).toBe(true);
      expect(
        antragListFilterToDTO({ ...testFilters, art: undefined }).istFehlbetrag
      ).toBeUndefined();
    });
  });
});
