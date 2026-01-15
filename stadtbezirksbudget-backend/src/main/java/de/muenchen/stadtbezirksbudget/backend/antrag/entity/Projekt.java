package de.muenchen.stadtbezirksbudget.backend.antrag.entity;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * This class represents a project that includes a title, description, start date, and end date.
 */
@Getter
@Setter
@Builder
@Embeddable
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class Projekt implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @NotBlank private String titel;
    @NotNull private LocalDate start;
    @NotNull private String fristBruchBegruendung;
    @NotNull private LocalDate ende;
    @NotBlank private String beschreibung;
}
