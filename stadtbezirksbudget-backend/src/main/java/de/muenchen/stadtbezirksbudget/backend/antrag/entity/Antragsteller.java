package de.muenchen.stadtbezirksbudget.backend.antrag.entity;

import de.muenchen.stadtbezirksbudget.backend.common.BaseEntity;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.io.Serial;
import lombok.AccessLevel;
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
@Embeddable
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class Antragsteller extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 1L;

    @NotBlank private String name;
    @NotBlank private String telefonNr;
    @NotBlank private String email;
    @Enumerated(EnumType.STRING)
    private Rechtsform rechtsform;
    @NotBlank private String zielsetzung;
    @NotNull @Embedded
    private Adresse adresse;
}
