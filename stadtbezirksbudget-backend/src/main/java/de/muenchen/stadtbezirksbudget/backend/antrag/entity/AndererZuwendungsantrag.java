package de.muenchen.stadtbezirksbudget.backend.antrag.entity;

import de.muenchen.stadtbezirksbudget.backend.common.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.io.Serial;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents another grant application that is linked to an existing application.
 * Contains the application date and the agency that submitted the application.
 */
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AndererZuwendungsantrag extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull private LocalDate antragsdatum;
    @NotBlank private String stelle;

    @NotNull @ManyToOne
    private Antrag antrag;
}
