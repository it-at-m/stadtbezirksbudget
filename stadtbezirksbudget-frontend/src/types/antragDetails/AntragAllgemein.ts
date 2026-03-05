import type { Status } from "@/types/antrag/Status.ts";
import type { AntragAndererZuwendungsantrag } from "@/types/antragDetails/AntragAndererZuwendungsantrag.ts";
import type { BeschlussStatus } from "@/types/antragDetails/enums/BeschlussStatus.ts";

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
  andereZuwendungsantraege: AntragAndererZuwendungsantrag[];
}
