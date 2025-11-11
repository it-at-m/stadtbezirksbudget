import { beforeEach, describe, expect, test, vi } from "vitest";

import { updateAntragStatus } from "@/api/update-antragStatus.ts";
import { useAntragStatusSelect } from "@/composables/antragStatusSelect";
import { STATUS_INDICATORS } from "@/constants.ts";
import { useSnackbarStore } from "@/stores/snackbar.ts";
import { Status } from "@/types/Status.ts";

vi.mock("@/api/update-antragStatus.ts");
vi.mock("@/stores/snackbar.ts");

describe("useAntragStatusSelect (composable)", () => {
  let snackbarStoreMock: { showMessage: ReturnType<typeof vi.fn> };

  beforeEach(() => {
    snackbarStoreMock = { showMessage: vi.fn() };
    (useSnackbarStore as vi.Mock).mockReturnValue(snackbarStoreMock);
  });

  test("updates status on successful API call and shows success snackbar", async () => {
    (updateAntragStatus as vi.Mock).mockResolvedValue(undefined);

    const { status, updateStatus } = useAntragStatusSelect(
      "1",
      Status.EINGEGANGEN
    );

    expect(status.value).toBe(Status.EINGEGANGEN);

    updateStatus(Status.ABGELEHNT_KEINE_RUECKMELDUNG);

    await new Promise((r) => setTimeout(r, 0));

    expect(status.value).toBe(Status.ABGELEHNT_KEINE_RUECKMELDUNG);
    expect(snackbarStoreMock.showMessage).toHaveBeenCalledWith({
      message: `Antragsstatus aktualisiert`,
      level: STATUS_INDICATORS.SUCCESS,
    });
  });

  test("shows API error message in snackbar on rejected promise", async () => {
    (updateAntragStatus as vi.Mock).mockRejectedValue(new Error("API Error"));

    const { status, updateStatus } = useAntragStatusSelect(
      "1",
      Status.EINGEGANGEN
    );

    updateStatus(Status.ABGELEHNT_NICHT_ZUSTAENDIG);

    await new Promise((r) => setTimeout(r, 0));

    expect(status.value).toBe(Status.EINGEGANGEN);
    expect(snackbarStoreMock.showMessage).toHaveBeenCalledWith({
      message: "API Error",
      level: STATUS_INDICATORS.WARNING,
    });
  });

  test("shows generic error message when rejected without message", async () => {
    (updateAntragStatus as vi.Mock).mockRejectedValue({});

    const { status, updateStatus } = useAntragStatusSelect(
      "1",
      Status.EINGEGANGEN
    );

    updateStatus(Status.ABGELEHNT_VON_BA);

    await new Promise((r) => setTimeout(r, 0));

    expect(status.value).toBe(Status.EINGEGANGEN);
    expect(snackbarStoreMock.showMessage).toHaveBeenCalledWith({
      message: "Fehler beim Aktualisieren des Antragsstatus",
      level: STATUS_INDICATORS.WARNING,
    });
  });
});
