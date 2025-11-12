import { beforeEach, describe, expect, test, vi } from "vitest";
import { ref } from "vue";

import { updateAntragStatus } from "@/api/update-antragStatus.ts";
import { useAntragStatusSelect } from "@/composables/antragStatusSelect";
import { STATUS_INDICATORS } from "@/constants.ts";
import { useSnackbarStore } from "@/stores/snackbar.ts";
import { Status, StatusOption, StatusText } from "@/types/Status.ts";

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
      ref(Status.EINGEGANGEN)
    );

    expect(status.value).toBe(Status.EINGEGANGEN);

    updateStatus(Status.ABGELEHNT_KEINE_RUECKMELDUNG);

    expect(updateAntragStatus).toHaveBeenCalledWith(
      "1",
      Status.ABGELEHNT_KEINE_RUECKMELDUNG
    );
    await vi.waitFor(() => {
      expect(status.value).toBe(Status.ABGELEHNT_KEINE_RUECKMELDUNG);
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
      ref(Status.EINGEGANGEN)
    );

    updateStatus(Status.ABGELEHNT_NICHT_ZUSTAENDIG);

    expect(updateAntragStatus).toHaveBeenCalledWith(
      "1",
      Status.ABGELEHNT_NICHT_ZUSTAENDIG
    );
    await vi.waitFor(() => {
      expect(status.value).toBe(Status.EINGEGANGEN);
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
      ref(Status.EINGEGANGEN)
    );

    updateStatus(Status.ABGELEHNT_VON_BA);

    expect(updateAntragStatus).toHaveBeenCalledWith(
      "1",
      Status.ABGELEHNT_VON_BA
    );
    await vi.waitFor(() => {
      expect(status.value).toBe(Status.EINGEGANGEN);
      expect(snackbarStoreMock.showMessage).toHaveBeenCalledWith({
        message: "Fehler beim Aktualisieren des Antragsstatus",
        level: STATUS_INDICATORS.WARNING,
      });
    });
  });

  test("testToggleStatusAndSearchWhenUnfocus", () => {
    const { status, toggleStatusAndSearch, search } = useAntragStatusSelect(
      ref("1"),
      ref(Status.EINGEGANGEN)
    );

    status.value = Status.ABGELEHNT_KEINE_RUECKMELDUNG;

    toggleStatusAndSearch(false);

    expect(status.value).toBe(Status.EINGEGANGEN);
    expect(search.value).toBe("");
  });

  test("testToggleStatusAndSearchWhenFocus", () => {
    const { status, toggleStatusAndSearch, search } = useAntragStatusSelect(
      ref("1"),
      ref(Status.EINGEGANGEN)
    );

    status.value = Status.ABGELEHNT_NICHT_ZUSTAENDIG;

    toggleStatusAndSearch(true);

    expect(status.value).toBeUndefined();
    expect(search.value).toBe(StatusText[Status.EINGEGANGEN].shortText);
  });

  test("testUpdateStatusReturnsEarlyWhenNewStatusIsFalsy", () => {
    const { status, updateStatus } = useAntragStatusSelect(
      ref("1"),
      ref(Status.EINGEGANGEN)
    );

    (updateAntragStatus as vi.Mock).mockClear();
    (updateAntragStatus as vi.Mock).mockResolvedValue(undefined);

    updateStatus(undefined as unknown as Status);

    expect(updateAntragStatus).not.toHaveBeenCalled();
    expect(status.value).toBe(Status.EINGEGANGEN);
    expect(snackbarStoreMock.showMessage).not.toHaveBeenCalled();
  });

  test("testStatusOptionsContainExpectedEntries", () => {
    const { statusOptions } = useAntragStatusSelect(
      ref("1"),
      ref(Status.EINGEGANGEN)
    );

    const eingegangenOption = statusOptions.find(
      (o: StatusOption) => o.value === Status.EINGEGANGEN
    );
    expect(eingegangenOption).toBeDefined();
    expect(eingegangenOption).toMatchObject({
      value: Status.EINGEGANGEN,
      ...StatusText[Status.EINGEGANGEN],
    });
  });

  test("testUpdatesStatusOnInitialStatusChange", async () => {
    const initialStatus = ref(Status.EINGEGANGEN);
    const { status } = useAntragStatusSelect(ref("1"), initialStatus);

    expect(status.value).toBe(Status.EINGEGANGEN);
    initialStatus.value = Status.VOLLSTAENDIG;
    await vi.waitFor(() => {
      expect(status.value).toBe(Status.VOLLSTAENDIG);
    });
  });
});
