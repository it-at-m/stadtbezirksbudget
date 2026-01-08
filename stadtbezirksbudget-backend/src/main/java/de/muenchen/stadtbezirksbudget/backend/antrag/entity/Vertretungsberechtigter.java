package de.muenchen.stadtbezirksbudget.backend.antrag.entity;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.EmbeddedColumnNaming;

/**
 * Represents a payment recipient with power of representation.
 * Contains information about the last name, first name, and mobile number.
 */
@Getter
@Setter
@Builder
@Embeddable
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class Vertretungsberechtigter implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @NotBlank private String nachname;
    @NotBlank private String vorname;
    @NotBlank private String telefonNr;
    @NotBlank private String email;
    @NotNull @Embedded
    @EmbeddedColumnNaming("adresse_%s")
    private Adresse adresse;
    @NotNull private String mobilNr;
}
