package de.muenchen.stadtbezirksbudget.backend.antrag.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import java.io.Serial;
import lombok.Getter;
import lombok.Setter;

/**
 * Repräsentiert einen Antragsteller, der Teil des Zahlungsempfängers ist.
 * Enthält Informationen über den Namen, die Zielsetzung und die Rechtsform des Antragstellers.
 */
@Entity
@Getter
@Setter
@DiscriminatorValue("Antragsteller")
public class Antragsteller extends Zahlungsempfaenger {
    @Serial
    private static final long serialVersionUID = 1L;

    @NotBlank private String name;

    @Enumerated(EnumType.STRING)
    private Rechtsform rechtsform;

    @NotBlank private String zielsetzung;
}
