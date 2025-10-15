package de.muenchen.stadtbezirksbudget.antrag.entities;

import de.muenchen.stadtbezirksbudget.backend.common.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.io.Serial;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class VoraussichtlicheAusgabe extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 1L;

    @NotBlank private String kategorie;
    @PositiveOrZero private double betrag;
    @NotNull private String direktoriumNotiz;
}
