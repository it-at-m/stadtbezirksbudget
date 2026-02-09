package de.muenchen.stadtbezirksbudget.backend.antrag.entity;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
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

@Getter
@Setter
@Builder
@Embeddable
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class Bezirksinformationen implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private LocalDate sitzungsDatum;
    @NotNull private String risNr;
    @NotNull @PositiveOrZero private BigDecimal bewilligteFoerderung;
    private LocalDate bescheidDatum;
}
