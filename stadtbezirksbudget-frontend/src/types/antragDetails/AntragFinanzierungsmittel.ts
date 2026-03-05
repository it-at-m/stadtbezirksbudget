import type { FinanzierungsmittelKategorie } from "@/types/antragDetails/enums/FinanzierungsmittelKategorie.ts";

export interface AntragFinanzierungsmittel {
  kategorie: FinanzierungsmittelKategorie;
  betrag: string;
  direktoriumNotiz: string;
}
