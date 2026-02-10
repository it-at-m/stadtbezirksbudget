package de.muenchen.stadtbezirksbudget.backend.antrag.entity;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents a payment entity with various attributes related to the payment.
 */
@Getter
@Setter
@Builder
@Embeddable
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class Zahlung implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull private String anlageAv;
    @NotNull private String anlageNr;
    @NotNull private String kreditor;
    @NotNull private String rechnungNr;
    @NotNull private String fiBelegNr;
    @NotNull private String bestellung;
    private BigDecimal auszahlungBetrag;
    private LocalDate auszahlungDatum;
}
