package de.muenchen.stadtbezirksbudget.backend.antrag.entity;

/**
 * The BeschlussStatus represent following cases:
 * BEWILLIGT: bezirksinformationen.bewilligteFoerderung is equal to finanzierung.beantragtesBudget
 * TEILBEWILLIGT: bezirksinformationen.bewilligteFoerderung is unequal to
 * finanzierung.beantragtesBudget AND > 0
 * ABGELEHNT: bezirksinformationen.bewilligteFoerderung is zero
 * LEER: bezirksinformationen.bewilligteFoerderung is null
 */
public enum BeschlussStatus {
    BEWILLIGT,
    TEILBEWILLIGT,
    ABGELEHNT,
    LEER
}
