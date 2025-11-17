import type { Status } from "@/types/Status.ts";

import { beforeEach, describe, expect, test, vi } from "vitest";
import { ref } from "vue";

import { updateAntragStatus } from "@/api/update-antragStatus.ts";
import { useAntragStatusSelect } from "@/composables/antragStatusSelect";
import { STATUS_INDICATORS } from "@/constants.ts";
import { useSnackbarStore } from "@/stores/snackbar.ts";
import { StatusOption, StatusText } from "@/types/Status.ts";

vi.mock("@/api/update-antragStatus.ts");
vi.mock("@/stores/snackbar.ts");

describe("useAntragStatusSelect", () => {
  let snackbarStoreMock: { showMessage: ReturnType<typeof vi.fn> };

  beforeEach(() => {
    snackbarStoreMock = { showMessage: vi.fn() };
    (useSnackbarStore as vi.Mock).mockReturnValue(snackbarStoreMock);
  });

  test("testUpdatesStatusOnSuccessfulApiCallAndShowsSuccessSnackbar", async () => {
    (updateAntragStatus as vi.Mock).mockResolvedValue(undefined);

    const { status, updateStatus } = useAntragStatusSelect(
      ref("1"),
      ref("EINGEGANGEN")
    );

    expect(status.value).toBe("EINGEGANGEN");

    updateStatus("ABGELEHNT_KEINE_RUECKMELDUNG");

    expect(updateAntragStatus).toHaveBeenCalledWith(
      "1",
      "ABGELEHNT_KEINE_RUECKMELDUNG"
    );
    await vi.waitFor(() => {
      expect(status.value).toBe("ABGELEHNT_KEINE_RUECKMELDUNG");
      expect(snackbarStoreMock.showMessage).toHaveBeenCalledWith({
        message: `Antragsstatus aktualisiert`,
        level: STATUS_INDICATORS.SUCCESS,
      });
    });
  });

  test("testShowsApiErrorMessageInSnackbarOnRejectedPromise", async () => {
    (updateAntragStatus as vi.Mock).mockRejectedValue(new Error("API Error"));

    const { status, updateStatus } = useAntragStatusSelect(
      ref("1"),
      ref("EINGEGANGEN")
    );

    updateStatus("ABGELEHNT_NICHT_ZUSTAENDIG");

    expect(updateAntragStatus).toHaveBeenCalledWith(
      "1",
      "ABGELEHNT_NICHT_ZUSTAENDIG"
    );
    await vi.waitFor(() => {
      expect(status.value).toBe("EINGEGANGEN");
      expect(snackbarStoreMock.showMessage).toHaveBeenCalledWith({
        message: "API Error",
        level: STATUS_INDICATORS.WARNING,
      });
    });
  });

  test("testShowsGenericErrorMessageWhenRejectedWithoutMessage", async () => {
    (updateAntragStatus as vi.Mock).mockRejectedValue({});

    const { status, updateStatus } = useAntragStatusSelect(
      ref("1"),
      ref("EINGEGANGEN")
    );

    updateStatus("ABGELEHNT_VON_BA");

    expect(updateAntragStatus).toHaveBeenCalledWith("1", "ABGELEHNT_VON_BA");
    await vi.waitFor(() => {
      expect(status.value).toBe("EINGEGANGEN");
      expect(snackbarStoreMock.showMessage).toHaveBeenCalledWith({
        message: "Fehler beim Aktualisieren des Antragsstatus",
        level: STATUS_INDICATORS.WARNING,
      });
    });
  });

  test("testToggleStatusAndSearchWhenUnfocus", () => {
    const { status, toggleStatusAndSearch, search } = useAntragStatusSelect(
      ref("1"),
      ref("EINGEGANGEN")
    );

    status.value = "ABGELEHNT_KEINE_RUECKMELDUNG";

    toggleStatusAndSearch(false);

    expect(status.value).toBe("EINGEGANGEN");
    expect(search.value).toBe("");
  });

  test("testToggleStatusAndSearchWhenFocus", () => {
    const { status, toggleStatusAndSearch, search } = useAntragStatusSelect(
      ref("1"),
      ref("EINGEGANGEN")
    );

    status.value = "ABGELEHNT_NICHT_ZUSTAENDIG";

    toggleStatusAndSearch(true);

    expect(status.value).toBeUndefined();
    expect(search.value).toBe(StatusText["EINGEGANGEN"].shortText);
  });

  test("testUpdateStatusReturnsEarlyWhenNewStatusIsFalsy", () => {
    const { status, updateStatus } = useAntragStatusSelect(
      ref("1"),
      ref("EINGEGANGEN")
    );

    (updateAntragStatus as vi.Mock).mockClear();
    (updateAntragStatus as vi.Mock).mockResolvedValue(undefined);

    updateStatus(undefined as unknown as Status);

    expect(updateAntragStatus).not.toHaveBeenCalled();
    expect(status.value).toBe("EINGEGANGEN");
    expect(snackbarStoreMock.showMessage).not.toHaveBeenCalled();
  });

  test("testStatusOptionsContainExpectedEntries", () => {
    const { statusOptions } = useAntragStatusSelect(
      ref("1"),
      ref("EINGEGANGEN")
    );

    const eingegangenOption = statusOptions.find(
      (o: StatusOption) => o.value === "EINGEGANGEN"
    );
    expect(eingegangenOption).toBeDefined();
    expect(eingegangenOption).toMatchObject({
      value: "EINGEGANGEN",
      ...StatusText["EINGEGANGEN"],
    });
  });

  test("testUpdatesStatusOnInitialStatusChange", async () => {
    const initialStatus = ref("EINGEGANGEN");
    const { status } = useAntragStatusSelect(ref("1"), initialStatus);

    expect(status.value).toBe("EINGEGANGEN");
    initialStatus.value = "VOLLSTAENDIG";
    await vi.waitFor(() => {
      expect(status.value).toBe("VOLLSTAENDIG");
    });
  });

  test("testHandlesConcurrentStatusUpdates", async () => {
    let resolveFirst: (value?: unknown) => void;
    let resolveSecond: (value?: unknown) => void;

    (updateAntragStatus as vi.Mock)
      .mockReturnValueOnce(
        new Promise((resolve) => {
          resolveFirst = resolve;
        })
      )
      .mockReturnValueOnce(
        new Promise((resolve) => {
          resolveSecond = resolve;
        })
      );

    const { status, updateStatus } = useAntragStatusSelect(
      ref("1"),
      ref("EINGEGANGEN")
    );

    updateStatus("VOLLSTAENDIG");
    updateStatus("ABGELEHNT_VON_BA");
    resolveSecond();
    await vi.waitFor(() => {
      expect(status.value).toBe("ABGELEHNT_VON_BA");
    });

    resolveFirst();
    await vi.waitFor(() => {
      expect(status.value).toBe("ABGELEHNT_VON_BA");
    });

    expect(updateAntragStatus).toHaveBeenCalledTimes(2);
    expect(updateAntragStatus).toHaveBeenNthCalledWith(1, "1", "VOLLSTAENDIG");
    expect(updateAntragStatus).toHaveBeenNthCalledWith(
      2,
      "1",
      "ABGELEHNT_VON_BA"
    );
    expect(snackbarStoreMock.showMessage).toHaveBeenCalled();
  });
});
