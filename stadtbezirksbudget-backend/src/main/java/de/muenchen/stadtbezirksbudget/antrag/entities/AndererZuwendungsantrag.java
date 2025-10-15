package de.muenchen.stadtbezirksbudget.antrag.entities;

import de.muenchen.stadtbezirksbudget.backend.common.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.io.Serial;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class AndererZuwendungsantrag extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull private LocalDate antragsdatum;
    @NotBlank private String stelle;
}
