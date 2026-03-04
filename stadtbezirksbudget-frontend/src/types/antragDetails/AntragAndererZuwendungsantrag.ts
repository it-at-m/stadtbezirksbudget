import type { AndererZuwendungsantragStatus } from "@/types/antrag/AndererZuwendungsantragStatus.ts";

export interface AntragAndererZuwendungsantrag {
  antragsdatum: string; // ISO date string from API
  stelle: string;
  betrag: number;
  status: AndererZuwendungsantragStatus;
}
