package de.muenchen.stadtbezirksbudget.backend.antrag.entity;

/**
 * The BeschlussStatus represent following cases:
 * <p>
 * {@link BeschlussStatus#BEWILLIGT}: bezirksinformationen.bewilligteFoerderung is equal to
 * finanzierung.beantragtesBudget
 * {@link BeschlussStatus#TEILBEWILLIGT}: bezirksinformationen.bewilligteFoerderung is unequal to
 * finanzierung.beantragtesBudget AND > 0
 * {@link BeschlussStatus#ABGELEHNT}: bezirksinformationen.bewilligteFoerderung is zero
 * {@link BeschlussStatus#LEER}: bezirksinformationen.bewilligteFoerderung is null
 */
public enum BeschlussStatus {
    BEWILLIGT,
    TEILBEWILLIGT,
    ABGELEHNT,
    LEER
}
