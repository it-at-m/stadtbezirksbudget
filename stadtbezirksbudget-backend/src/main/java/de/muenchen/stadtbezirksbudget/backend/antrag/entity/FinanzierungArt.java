package de.muenchen.stadtbezirksbudget.backend.antrag.entity;

/**
 * The FinanzierungArt represent following cases:
 * <p>
 * {@link FinanzierungArt#FEHL}: The sum of all costs (VoraussichtlicheAusgabe), related to this
 * finanzierung, are greater than FEHL_ART_SCHWELLE OR the sum of all earnings (Finanzierungsmittel)
 * is greater than zero
 * <p>
 * {@link FinanzierungArt#FEST}: All other cases, meaning the sum of all earnings is zero and the
 * sum of all costs are smaller than FEHL_ART_SCHWELLE
 */
public enum FinanzierungArt {
    FEHL,
    FEST
}
