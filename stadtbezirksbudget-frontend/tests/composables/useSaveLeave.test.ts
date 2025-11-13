import { beforeEach, describe, expect, it, vi } from "vitest";

import { useSaveLeave } from "@/composables/saveLeave";

let registeredGuard:
  | null
  | ((to: object, from: object, next: () => void) => void) = null;

vi.mock("vue-router", () => {
  return {
    onBeforeRouteLeave: (
      guard: (to: object, from: object, next: () => void) => void
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

  it("testCallsNextIfNotDirty", async () => {
    const nextMock = vi.fn();
    const { saveLeaveDialog } = useSaveLeave(() => false);

    expect(registeredGuard).not.toBeNull();

    (registeredGuard as (to: object, from: object, next: () => void) => void)(
      {},
      {},
      nextMock
    );

    expect(nextMock).toHaveBeenCalled();
    expect(saveLeaveDialog.value).toBe(false);
  });

  it("testShowsDialogAndSaves", async () => {
    const nextMock = vi.fn();
    const { saveLeaveDialog, leave } = useSaveLeave(() => true);

    expect(registeredGuard).not.toBeNull();

    (registeredGuard as (to: object, from: object, next: () => void) => void)(
      {},
      {},
      nextMock
    );

    expect(saveLeaveDialog.value).toBe(true);
    expect(nextMock).not.toHaveBeenCalled();

    leave();
    expect(nextMock).toHaveBeenCalled();
  });

  it("testCancelCallsNextAndClosesDialog", async () => {
    const nextMock = vi.fn();
    const { saveLeaveDialog, cancel } = useSaveLeave(() => true);

    (registeredGuard as (to: object, from: object, next: () => void) => void)(
      {},
      {},
      nextMock
    );

    expect(saveLeaveDialog.value).toBe(true);

    cancel();

    expect(nextMock).toHaveBeenCalledWith(false);
    expect(saveLeaveDialog.value).toBe(false);
  });

  it("testCancelCallsNextAndClosesDialog", async () => {
    const nextMock = vi.fn();
    const { saveLeaveDialog, cancel } = useSaveLeave(() => true);

    (registeredGuard as (to: object, from: object, next: () => void) => void)(
      {},
      {},
      nextMock
    );

    expect(saveLeaveDialog.value).toBe(true);

    cancel();

    expect(nextMock).toHaveBeenCalledWith(false);
    expect(saveLeaveDialog.value).toBe(false);
  });

  it("testBypassesDialogIfSave", async () => {
    const nextMock = vi.fn();
    const instance = useSaveLeave(() => true);
    instance.isSave.value = true;

    (registeredGuard as (to: object, from: object, next: () => void) => void)(
      {},
      {},
      nextMock
    );

    expect(nextMock).toHaveBeenCalled();
    expect(instance.saveLeaveDialog.value).toBe(false);
  });

  it("testLeaveWithoutNextDoesNotThrow", () => {
    const instance = useSaveLeave(() => true);
    expect(() => instance.leave()).not.toThrow();
    expect(() => instance.cancel()).not.toThrow();
  });
});
