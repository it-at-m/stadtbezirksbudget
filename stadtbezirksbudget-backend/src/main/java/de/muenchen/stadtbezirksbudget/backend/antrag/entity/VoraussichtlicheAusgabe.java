package de.muenchen.stadtbezirksbudget.backend.antrag.entity;

import de.muenchen.stadtbezirksbudget.backend.common.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.io.Serial;
import lombok.Getter;
import lombok.Setter;

/**
 * Repräsentiert eine voraussichtliche Ausgabe im Rahmen einer Finanzierung.
 * Enthält Informationen über die Kategorie, den Betrag und Notizen zum Direktorium.
 */
@Entity
@Getter
@Setter
public class VoraussichtlicheAusgabe extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 1L;

    @NotBlank private String kategorie;
    @PositiveOrZero private double betrag;
    @NotNull private String direktoriumNotiz;

    @NotNull @ManyToOne
    private Finanzierung finanzierung;
}
