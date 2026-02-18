import { describe, expect, test, vi } from "vitest";

import { useReferenceLink } from "@/composables/useReferenceLink.ts";
import { useConfigStore } from "@/stores/useConfigStore.ts";

vi.mock("@/stores/useConfigStore.ts");

describe("useReferenceLink", () => {
  describe("getZammadLink", () => {
    test("formats link with base url", () => {
      (useConfigStore as vi.Mock).mockReturnValue({
        getZammadBaseUrl: "https://zammad.muenchen.invalid",
      });

      const { getZammadLink } = useReferenceLink();
      expect(getZammadLink("00000018").value).toBe(
        "https://zammad.muenchen.invalid/#ticket/zoom/00000018"
      );
    });

    test("returns undefined on undefined base url", () => {
      (useConfigStore as vi.Mock).mockReturnValue({
        getZammadBaseUrl: undefined,
      });

      const { getZammadLink } = useReferenceLink();
      expect(getZammadLink("00000018").value).toBeUndefined();
    });
  });

  describe("getEakteLink", () => {
    test("formats link with base url", () => {
      (useConfigStore as vi.Mock).mockReturnValue({
        getEakteBaseUrl: "https://akte.muenchen.invalid",
      });

      const { getEakteLink } = useReferenceLink();
      expect(getEakteLink("COO.1234.5678.9.0123456").value).toBe(
        "https://akte.muenchen.invalid/fsc/fscasp/content/bin/fscvext.dll?bx=COO.1234.5678.9.0123456"
      );
    });

    test("returns undefined on undefined base url", () => {
      (useConfigStore as vi.Mock).mockReturnValue({
        getEakteBaseUrl: undefined,
      });

      const { getEakteLink } = useReferenceLink();
      expect(getEakteLink("COO.1234.5678.9.0123456").value).toBeUndefined();
    });
  });
});
