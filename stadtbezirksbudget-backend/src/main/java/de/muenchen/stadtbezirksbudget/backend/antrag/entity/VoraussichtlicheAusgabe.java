package de.muenchen.stadtbezirksbudget.backend.antrag.entity;

import de.muenchen.stadtbezirksbudget.backend.common.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.io.Serial;
import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents an anticipated expenditure in the context of financing.
 * Contains information about the category, the amount, and notes for the board of directors.
 */
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class VoraussichtlicheAusgabe extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 1L;

    @NotBlank private String kategorie;
    @NotNull @PositiveOrZero private BigDecimal betrag;
    @NotNull private String direktoriumNotiz;

    @NotNull @ManyToOne
    private Antrag antrag;
}
