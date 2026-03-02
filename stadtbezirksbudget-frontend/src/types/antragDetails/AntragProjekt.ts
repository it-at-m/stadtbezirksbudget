export interface AntragProjekt {
  titel: string;
  beschreibung: string;
  rubrik: string;
  start: string; // ISO date string from API
  ende: string; // ISO date string from API
  fristBruchBegruendung: string;
}
