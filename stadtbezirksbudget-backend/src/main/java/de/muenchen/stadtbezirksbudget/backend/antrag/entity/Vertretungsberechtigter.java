package de.muenchen.stadtbezirksbudget.backend.antrag.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import java.io.Serial;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@DiscriminatorValue("Vertretungsberechtigter")
public class Vertretungsberechtigter extends Zahlungsempfaenger {
    @Serial
    private static final long serialVersionUID = 1L;

    @NotBlank private String nachname;
    @NotBlank private String vorname;
    @NotBlank private String mobilNr;
}
