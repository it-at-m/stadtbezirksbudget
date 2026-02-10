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
 * Represents a proof of use entity with various attributes related to the usage of funds.
 */
@Getter
@Setter
@Builder
@Embeddable
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class Verwendungsnachweis implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private BigDecimal betrag;
    @NotNull private String status;
    private BigDecimal pruefungBetrag;
    private LocalDate buchungsDatum;
    private LocalDate sapEingangsdatum;
}
