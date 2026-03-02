import type { BeschlussStatus } from "@/types/antrag/BeschlussStatus.ts";
import type { Status } from "@/types/Status.ts";

export interface AntragAllgemein {
  eingangDatum: string; // ISO date string from API
  status: Status;
  zammadTicketNr: string;
  aktenzeichen: string;
  eakteCooAdresse: string;
  istGegendert: boolean;
  anmerkungen: string;
  beschlussStatus: BeschlussStatus;
  istZuwendungDritterBeantragt: boolean;
  summeAndereZuwendungsantraege: number;
}
