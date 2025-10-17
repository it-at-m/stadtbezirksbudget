package de.muenchen.stadtbezirksbudget.backend.antrag.entity;

import de.muenchen.stadtbezirksbudget.backend.common.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.io.Serial;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "titel", "beschreibung", "start", "ende" }))
public class Projekt extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 1L;

    @NotBlank private String titel;
    @NotBlank private String beschreibung;
    @NotNull private LocalDate start;
    @NotNull private LocalDate ende;
}
