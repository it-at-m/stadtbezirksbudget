import type { FinanzierungsmittelKategorie } from "@/types/antrag/FinanzierungsmittelKategorie.ts";

export interface AntragFinanzierungsmittel {
  kategorie: FinanzierungsmittelKategorie;
  betrag: string;
  direktoriumNotiz: string;
}
