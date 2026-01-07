package de.muenchen.stadtbezirksbudget.backend.antrag.entity;

import de.muenchen.stadtbezirksbudget.backend.common.BaseEntity;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
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
 * Represents a payment recipient with power of representation.
 * Contains information about the last name, first name, and mobile number.
 */
@Entity
@Getter
@Setter
@Builder
@Embeddable
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class Vertretungsberechtigter extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 1L;

    @NotBlank private String nachname;
    @NotBlank private String vorname;
    @NotBlank private String telefonNr;
    @NotBlank private String email;
    @NotNull @ManyToOne
    private Adresse adresse;
    private String mobilNr;
}
