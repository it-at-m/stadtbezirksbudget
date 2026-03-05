export interface AntragZahlung {
  auszahlungBetrag?: number;
  auszahlungDatum?: string; // ISO date string from API
  anlageAv: string;
  anlageNr: string;
  kreditor: string;
  rechnungNr: string;
  fiBelegNr: string;
  bestellung: string;
}
