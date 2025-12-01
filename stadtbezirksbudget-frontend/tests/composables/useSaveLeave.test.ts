import { beforeEach, describe, expect, test, vi } from "vitest";

import { useSaveLeave } from "@/composables/saveLeave";

let registeredGuard:
  | null
  | ((to: object, from: object, next: (to?: false | object) => void) => void) =
  null;

vi.mock("vue-router", () => {
  return {
    onBeforeRouteLeave: (
      guard: (
        to: object,
        from: object,
        next: (to?: false | object) => void
      ) => void
    ) => {
      registeredGuard = guard;
    },
  };
});

describe("useSaveLeave", () => {
  beforeEach(() => {
    registeredGuard = null;
    vi.clearAllMocks();
  });

  function invokeGuard(nextMock: ReturnType<typeof vi.fn>) {
    if (!registeredGuard) throw new Error("Guard not registered");
    registeredGuard({}, {}, nextMock as (to?: false | object) => void);
  }

  test("calls next if not dirty", () => {
    const nextMock = vi.fn();
    const { saveLeaveDialog } = useSaveLeave(() => false);

    expect(registeredGuard).not.toBeNull();

    invokeGuard(nextMock);

    expect(nextMock).toHaveBeenCalled();
    expect(saveLeaveDialog.value).toBe(false);
  });

  test("shows dialog and saves", () => {
    const nextMock = vi.fn();
    const { saveLeaveDialog, leave } = useSaveLeave(() => true);

    expect(registeredGuard).not.toBeNull();

    invokeGuard(nextMock);

    expect(saveLeaveDialog.value).toBe(true);
    expect(nextMock).not.toHaveBeenCalled();

    leave();
    expect(nextMock).toHaveBeenCalled();
  });

  test("cancel calls next and closes dialog", () => {
    const nextMock = vi.fn();
    const { saveLeaveDialog, cancel } = useSaveLeave(() => true);

    expect(registeredGuard).not.toBeNull();

    invokeGuard(nextMock);

    expect(saveLeaveDialog.value).toBe(true);

    cancel();

    expect(nextMock).toHaveBeenCalledWith(false);
    expect(saveLeaveDialog.value).toBe(false);
  });

  test("bypasses dialog if save", () => {
    const nextMock = vi.fn();
    const instance = useSaveLeave(() => true);
    instance.isSave.value = true;

    expect(registeredGuard).not.toBeNull();

    invokeGuard(nextMock);

    expect(nextMock).toHaveBeenCalled();
    expect(instance.saveLeaveDialog.value).toBe(false);
  });

  test("leave without next doesn't throw", () => {
    const instance = useSaveLeave(() => true);
    expect(() => instance.leave()).not.toThrow();
  });

  test("cancel without next doesn't throw", () => {
    const instance = useSaveLeave(() => true);
    expect(() => instance.cancel()).not.toThrow();
  });
});
