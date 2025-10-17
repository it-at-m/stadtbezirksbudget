package de.muenchen.stadtbezirksbudget.backend.antrag.entity;

import de.muenchen.stadtbezirksbudget.backend.common.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.io.Serial;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Finanzierungsmittel extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull @Enumerated(EnumType.STRING)
    private Kategorie kategorie;
    @PositiveOrZero private double betrag;
    @NotNull private String direktoriumNotiz;

    @NotNull @ManyToOne
    private Finanzierung finanzierung;
}
