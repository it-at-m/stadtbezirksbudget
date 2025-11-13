import { beforeEach, describe, expect, it, vi } from "vitest";

import { useSaveLeave } from "@/src/composables/saveLeave";


let registeredGuard: null | ((to: object, from: object, next: () => void) => void) = null;

vi.mock("vue-router", () => {
  return {
    onBeforeRouteLeave: (guard: (to: object, from: object, next: () => void) => void) => {
      registeredGuard = guard;
    },
  };
});

describe("useSaveLeave", () => {
  beforeEach(() => {
    registeredGuard = null;
    vi.clearAllMocks();
  });

  it("testCallsNextIfNotDirty", async () => {
    const nextMock = vi.fn();
    const { saveLeaveDialog } = useSaveLeave(() => false);

    expect(registeredGuard).not.toBeNull();

    (registeredGuard as (to: object, from: object, next: () => void) => void)({}, {}, nextMock);

    expect(nextMock).toHaveBeenCalled();
    expect(saveLeaveDialog.value).toBe(false);
  });
});
