import type { Status } from "@/types/Status.ts";

import { beforeEach, describe, expect, test, vi } from "vitest";
import { ref } from "vue";

import { updateAntragStatus } from "@/api/update-antragStatus.ts";
import { useAntragStatusUpdate } from "@/composables/useAntragStatusUpdate.ts";
import { STATUS_INDICATORS } from "@/constants.ts";
import { useSnackbarStore } from "@/stores/useSnackbarStore.ts";

vi.mock("@/api/update-antragStatus.ts");
vi.mock("@/stores/useSnackbarStore.ts");

describe("useAntragStatusUpdate", () => {
  let snackbarStoreMock: { showMessage: ReturnType<typeof vi.fn> };
  const onUpdate = vi.fn();

  beforeEach(() => {
    snackbarStoreMock = { showMessage: vi.fn() };
    (useSnackbarStore as vi.Mock).mockReturnValue(snackbarStoreMock);
    onUpdate.mockReset();
  });

  describe("updateStatus", () => {
    test("updates status on successful api call and shows success snackbar", async () => {
      (updateAntragStatus as vi.Mock).mockResolvedValue(undefined);

      const { status, updateStatus } = useAntragStatusUpdate(
        ref("1"),
        ref("EINGEGANGEN"),
        onUpdate
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
        expect(onUpdate).toHaveBeenCalledWith("ABGELEHNT_KEINE_RUECKMELDUNG");
      });
    });

    test("handles api errors when updating status", async () => {
      (updateAntragStatus as vi.Mock).mockRejectedValue(new Error("API Error"));

      const { status, updateStatus } = useAntragStatusUpdate(
        ref("1"),
        ref("EINGEGANGEN"),
        onUpdate
      );

      updateStatus("ABGELEHNT_NICHT_ZUSTAENDIG");

      expect(updateAntragStatus).toHaveBeenCalledWith(
        "1",
        "ABGELEHNT_NICHT_ZUSTAENDIG"
      );
      await vi.waitFor(() => {
        expect(status.value).toBe("EINGEGANGEN");
        expect(snackbarStoreMock.showMessage).toHaveBeenCalledWith({
          message: "Fehler beim Aktualisieren des Antragsstatus",
          level: STATUS_INDICATORS.WARNING,
        });
        expect(onUpdate).not.toHaveBeenCalled();
      });
    });

    test("handles generic errors when updating status", async () => {
      (updateAntragStatus as vi.Mock).mockRejectedValue({});

      const { status, updateStatus } = useAntragStatusUpdate(
        ref("1"),
        ref("EINGEGANGEN"),
        onUpdate
      );

      updateStatus("ABGELEHNT_VON_BA");

      expect(updateAntragStatus).toHaveBeenCalledWith("1", "ABGELEHNT_VON_BA");
      await vi.waitFor(() => {
        expect(status.value).toBe("EINGEGANGEN");
        expect(snackbarStoreMock.showMessage).toHaveBeenCalledWith({
          message: "Fehler beim Aktualisieren des Antragsstatus",
          level: STATUS_INDICATORS.WARNING,
        });
        expect(onUpdate).not.toHaveBeenCalled();
      });
    });

    test("doesn't update status when new status is not set", () => {
      const { status, updateStatus } = useAntragStatusUpdate(
        ref("1"),
        ref("EINGEGANGEN"),
        onUpdate
      );

      (updateAntragStatus as vi.Mock).mockClear();
      (updateAntragStatus as vi.Mock).mockResolvedValue(undefined);

      updateStatus(undefined as unknown as Status);

      expect(updateAntragStatus).not.toHaveBeenCalled();
      expect(status.value).toBe("EINGEGANGEN");
      expect(snackbarStoreMock.showMessage).not.toHaveBeenCalled();
      expect(onUpdate).not.toHaveBeenCalled();
    });

    test("handles concurrent status updates", async () => {
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

      const { status, updateStatus } = useAntragStatusUpdate(
        ref("1"),
        ref("EINGEGANGEN"),
        onUpdate
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
      expect(updateAntragStatus).toHaveBeenNthCalledWith(
        1,
        "1",
        "VOLLSTAENDIG"
      );
      expect(updateAntragStatus).toHaveBeenNthCalledWith(
        2,
        "1",
        "ABGELEHNT_VON_BA"
      );
      expect(onUpdate).toHaveBeenNthCalledWith(1, "ABGELEHNT_VON_BA");
      expect(onUpdate).toHaveBeenNthCalledWith(2, "VOLLSTAENDIG");
      expect(snackbarStoreMock.showMessage).toHaveBeenCalled();
    });

    test("updates status on initial status change", async () => {
      const initialStatus = ref("EINGEGANGEN");
      const { status } = useAntragStatusUpdate(
        ref("1"),
        initialStatus,
        onUpdate
      );

      expect(status.value).toBe("EINGEGANGEN");
      initialStatus.value = "VOLLSTAENDIG";
      await vi.waitFor(() => {
        expect(status.value).toBe("VOLLSTAENDIG");
        expect(onUpdate).not.toHaveBeenCalled();
      });
    });
  });

  describe("onFocusChange", () => {
    test("resets status when unfocused", () => {
      const { status, onFocusChange, search } = useAntragStatusUpdate(
        ref("1"),
        ref("EINGEGANGEN"),
        onUpdate
      );

      status.value = "ABGELEHNT_KEINE_RUECKMELDUNG";

      onFocusChange(false);

      expect(status.value).toBe("EINGEGANGEN");
      expect(search.value).toBe("");
      expect(onUpdate).not.toHaveBeenCalled();
    });
  });
});
