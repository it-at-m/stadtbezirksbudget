import { beforeEach, describe, expect, test, vi } from "vitest";

import { useAntragListSearch } from "@/composables/useAntragListSearch.ts";
import { useAntragListFilterStore } from "@/stores/useAntragListFilterStore.ts";
import { defaultAntragListFilter } from "@/types/AntragListFilter";

vi.mock("@/stores/useAntragListFilterStore.ts");

describe("useAntragListSearch", () => {
  let filterStoreMock;

  beforeEach(() => {
    filterStoreMock = {
      filters: {
        ...defaultAntragListFilter(),
        search: "test search",
      },
      setFilters: vi.fn(),
      setSearch: vi.fn(),
    };
    (useAntragListFilterStore as vi.Mock).mockReturnValue(filterStoreMock);
  });

  test("gets initial search from store", () => {
    const { query } = useAntragListSearch();
    expect(query.value).toBe(filterStoreMock.filters.search);
  });

  describe("search", () => {
    test("search calls setSearch", async () => {
      const { query, search } = useAntragListSearch();
      query.value = "new search";

      search();
      expect(filterStoreMock.setSearch).toHaveBeenCalledWith(query.value);
    });
  });
});
