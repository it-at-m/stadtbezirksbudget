import { beforeEach, describe, expect, test, vi } from "vitest";
import { useRoute } from "vue-router";

import { useRouteCheck } from "@/composables/useRouteCheck.ts";
import { ROUTES_DETAILS, ROUTES_HOME } from "@/constants.ts";

vi.mock("vue-router", () => {
  return {
    useRoute: vi.fn(),
  };
});

type ResultType = ReturnType<typeof useRouteCheck>;
type UseRoute = ReturnType<typeof useRoute>;
type FlagKey = keyof ResultType;

describe("useRouteCheck", () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  const cases: {
    routeName: string;
    trueFlags: FlagKey[];
  }[] = [
    { routeName: ROUTES_HOME, trueFlags: ["isHomePage"] },
    {
      routeName: ROUTES_DETAILS,
      trueFlags: ["isDetailsPage"],
    },
    { routeName: "other", trueFlags: [] },
  ];

  test.each(cases)(
    "$routeName -> only $trueFlags are true",
    ({ routeName, trueFlags }) => {
      vi.mocked(useRoute).mockReturnValue({ name: routeName } as UseRoute);

      const result = useRouteCheck();

      const keys = Object.keys(result) as FlagKey[];
      keys.forEach((key) => {
        const refValue = result[key] as { value: boolean };
        const shouldBeTrue = trueFlags.includes(key);
        expect(refValue.value).toBe(shouldBeTrue);
      });
    }
  );
});
