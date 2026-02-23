import { beforeEach, describe, expect, test, vi } from "vitest";
import { ref } from "vue";

import { updateAntragReference } from "@/api/update-antragReference.ts";
import { useEakteCooAdresseEdit } from "@/composables/useEakteCooAdresseEdit.ts";
import { STATUS_INDICATORS } from "@/constants.ts";
import { useSnackbarStore } from "@/stores/useSnackbarStore.ts";

vi.mock("@/api/update-antragReference.ts");
vi.mock("@/stores/useSnackbarStore.ts");

describe("useEakteCooAdresseEdit", () => {
  let snackbarStoreMock: { showMessage: ReturnType<typeof vi.fn> };
  const onSave = vi.fn();

  beforeEach(() => {
    vi.clearAllMocks();
    snackbarStoreMock = { showMessage: vi.fn() };
    useSnackbarStore.mockReturnValue(snackbarStoreMock);
  });

  describe("save", () => {
    test("updates reference on successful api call and shows success snackbar", async () => {
      updateAntragReference.mockResolvedValue(undefined);

      const { eakteCooAdresse, isValid, save } = useEakteCooAdresseEdit(
        ref("1"),
        ref("COO.6804.7915.3.3210800"),
        onSave
      );
      expect(eakteCooAdresse.value).toBe("COO.6804.7915.3.3210800");
      eakteCooAdresse.value = "COO.6804.7915.3.3210801";
      isValid.value = true;

      save();
      expect(updateAntragReference).toHaveBeenCalledWith("1", {
        eakteCooAdresse: "COO.6804.7915.3.3210801",
      });
      await vi.waitFor(() => {
        expect(snackbarStoreMock.showMessage).toHaveBeenCalledWith({
          message: "COO-Adresse aktualisiert",
          level: STATUS_INDICATORS.SUCCESS,
        });
        expect(onSave).toHaveBeenCalled();
      });
    });

    test("handles errors when updating reference", async () => {
      updateAntragReference.mockRejectedValue(new Error("API Error"));

      const { eakteCooAdresse, isValid, save } = useEakteCooAdresseEdit(
        ref("1"),
        ref("COO.6804.7915.3.3210800"),
        onSave
      );
      eakteCooAdresse.value = "COO.6804.7915.3.3210801";
      isValid.value = true;
      save();

      expect(updateAntragReference).toHaveBeenCalledWith("1", {
        eakteCooAdresse: "COO.6804.7915.3.3210801",
      });
      await vi.waitFor(() => {
        expect(snackbarStoreMock.showMessage).toHaveBeenCalledWith({
          message: "Fehler beim Aktualisieren der COO-Adresse",
          level: STATUS_INDICATORS.WARNING,
        });
        expect(onSave).not.toHaveBeenCalled();
      });
    });

    test("doesn't update reference when not changed", () => {
      const { save, isSaveable } = useEakteCooAdresseEdit(
        ref("1"),
        ref("COO.6804.7915.3.3210800"),
        onSave
      );
      save();

      expect(isSaveable.value).toBeFalsy();
      expect(updateAntragReference).not.toHaveBeenCalled();
      expect(snackbarStoreMock.showMessage).not.toHaveBeenCalled();
      expect(onSave).not.toHaveBeenCalled();
    });

    test("closes menu on successful save", async () => {
      updateAntragReference.mockResolvedValue(undefined);

      const { eakteCooAdresse, isValid, save, menu } = useEakteCooAdresseEdit(
        ref("1"),
        ref("COO.6804.7915.3.3210800"),
        onSave
      );
      eakteCooAdresse.value = "COO.6804.7915.3.3210801";
      isValid.value = true;
      menu.value = true;
      save();
      await vi.waitFor(() => {
        expect(menu.value).toBeFalsy();
      });
    });
  });

  describe("isSaveable", () => {
    test("is false on unchanged and not valid", async () => {
      const { isSaveable } = useEakteCooAdresseEdit(
        ref("1"),
        ref("COO.6804.7915.3.3210800"),
        onSave
      );
      expect(isSaveable.value).toBeFalsy();
    });

    test("is false on unchanged and valid", async () => {
      const { isSaveable, isValid } = useEakteCooAdresseEdit(
        ref("1"),
        ref("COO.6804.7915.3.3210800"),
        onSave
      );
      isValid.value = true;
      expect(isSaveable.value).toBeFalsy();
    });

    test("is false on changed and not valid", async () => {
      const { eakteCooAdresse, isSaveable } = useEakteCooAdresseEdit(
        ref("1"),
        ref("COO.6804.7915.3.3210800"),
        onSave
      );
      eakteCooAdresse.value = "COO.6804.7915.3.3210801";
      expect(isSaveable.value).toBeFalsy();
    });

    test("is true on changed and valid", async () => {
      const { eakteCooAdresse, isSaveable, isValid } = useEakteCooAdresseEdit(
        ref("1"),
        ref("COO.6804.7915.3.3210800"),
        onSave
      );
      eakteCooAdresse.value = "COO.6804.7915.3.3210801";
      isValid.value = true;
      expect(isSaveable.value).toBeTruthy();
    });
  });

  describe("cancel", () => {
    test("closes menu", async () => {
      const { cancel, menu } = useEakteCooAdresseEdit(
        ref("1"),
        ref("COO.6804.7915.3.3210800"),
        onSave
      );
      menu.value = true;
      cancel();
      expect(menu.value).toBeFalsy();
    });
  });

  describe("validateCoo", () => {
    const { validateCoo } = useEakteCooAdresseEdit(
      ref("1"),
      ref("COO.6804.7915.3.3210800"),
      onSave
    );

    test("returns true on empty value", () => {
      expect(validateCoo("")).toBeTruthy();
    });

    test("returns true on correct value", () => {
      expect(validateCoo("COO.6804.7915.3.3210800")).toBeTruthy();
    });

    test("returns false on invalid value", () => {
      expect(validateCoo("COO.6804.7915.33210800")).toBe(
        'Muss dem Format "COO.XXXX.XXXX.X.XXXXXXX" entsprechen'
      );
    });
  });
});
