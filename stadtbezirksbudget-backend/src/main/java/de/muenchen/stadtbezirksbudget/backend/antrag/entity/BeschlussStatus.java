package de.muenchen.stadtbezirksbudget.backend.antrag.entity;

/**
 * The BeschlussStatus represent following cases:
 *  BEWILLIGT: The value of bezirksinformationen.bewilligteFoerderung is equal to the value of finanzierung.beantragtesBudget
 *  TEILBEWILLIGT: The value of bezirksinformationen.bewilligteFoerderung is unequal as the value of finanzierung.beantragtesBudget, but > 0
 *  ABGELEHNT: The value of bezirksinformationen.bewilligteFoerderung is zero
 *  LEER: The value of bezirksinformationen.bewilligteFoerderung is null
 */
public enum BeschlussStatus {
    BEWILLIGT,
    TEILBEWILLIGT,
    ABGELEHNT,
    LEER
}
