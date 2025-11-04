package de.muenchen.stadtbezirksbudget.backend.antrag.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.io.Serial;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents a payment recipient with power of representation.
 * Contains information about the last name, first name, and mobile number.
 */
@Entity
@Getter
@Setter
@DiscriminatorValue("Vertretungsberechtigter")
public class Vertretungsberechtigter extends Zahlungsempfaenger {
    @Serial
    private static final long serialVersionUID = 1L;

    @NotBlank private String nachname;
    @NotBlank private String vorname;
    private String mobilNr;
}
