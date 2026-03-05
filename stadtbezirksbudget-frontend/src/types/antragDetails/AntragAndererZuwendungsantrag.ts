import type { AndererZuwendungsantragStatus } from "@/types/antragDetails/enums/AndererZuwendungsantragStatus.ts";

export interface AntragAndererZuwendungsantrag {
  antragsdatum: string; // ISO date string from API
  stelle: string;
  betrag: number;
  status: AndererZuwendungsantragStatus;
}
