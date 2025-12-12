package de.muenchen.stadtbezirksbudget.backend.antrag.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import java.io.Serial;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents an applicant that is part of the payee.
 * Contains information about the applicant's name, purpose, and legal form.
 */
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DiscriminatorValue("Antragsteller")
public class Antragsteller extends Zahlungsempfaenger {
    @Serial
    private static final long serialVersionUID = 1L;

    @NotBlank private String name;

    @Enumerated(EnumType.STRING)
    private Rechtsform rechtsform;

    @NotBlank private String zielsetzung;
}
