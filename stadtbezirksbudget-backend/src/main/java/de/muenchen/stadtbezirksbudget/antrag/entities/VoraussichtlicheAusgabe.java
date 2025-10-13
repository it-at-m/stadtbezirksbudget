package de.muenchen.stadtbezirksbudget.antrag.entities;

import de.muenchen.stadtbezirksbudget.common.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class VoraussichtlicheAusgabe extends BaseEntity {
    @NotBlank private String kategorie;
    @PositiveOrZero private double betrag;
    @NotNull private String direktoriumNotiz;
}
