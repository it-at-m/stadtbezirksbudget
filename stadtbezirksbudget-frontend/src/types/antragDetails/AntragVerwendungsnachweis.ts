export interface AntragVerwendungsnachweis {
  betrag?: number;
  status: string;
  pruefungBetrag?: number;
  buchungsDatum?: string; // ISO date string from API
  sapEingangsdatum?: string; // ISO date string from API
}
