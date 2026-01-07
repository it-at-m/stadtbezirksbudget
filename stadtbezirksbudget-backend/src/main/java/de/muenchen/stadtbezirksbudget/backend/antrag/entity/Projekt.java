package de.muenchen.stadtbezirksbudget.backend.antrag.entity;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents a project with a title, description, start date, and end date.
 * The project is uniquely identified by the combination of its title, description, start date, and
 * end date.
 */
@Getter
@Setter
@Builder
@Embeddable
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class Projekt {
    @NotBlank private String titel;
    @NotNull private LocalDate start;
    private boolean istStartFrist;
    @NotNull private String fristBruchBegruendung;
    @NotNull private LocalDate ende;
    @NotBlank private String beschreibung;
}
