import type { AntragFinanzierungsmittel } from "@/types/antragDetails/AntragFinanzierungsmittel.ts";
import type { AntragVoraussichtlicheAusgabe } from "@/types/antragDetails/AntragVoraussichtlicheAusgabe.ts";

export interface AntragFinanzierung {
  beantragtesBudget: number;
  istPersonVorsteuerabzugsberechtigt: boolean;
  istProjektVorsteuerabzugsberechtigt: boolean;
  voraussichtlicheAusgaben: AntragVoraussichtlicheAusgabe[];
  gesamtkosten: number;
  kostenAnmerkung: string;
  istZuwendungDritterBeantragt: boolean;
  finanzierungsmittel: AntragFinanzierungsmittel[];
  istZuwenigEigenmittel: boolean;
  begruendungEigenmittel: string;
  gesamtmittel: number;
  istEinladungFoerderhinweis: boolean;
  istWebsiteFoerderhinweis: boolean;
  istSonstigerFoerderhinweis: boolean;
  sonstigeFoerderhinweise: boolean;
}
