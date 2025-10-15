package de.muenchen.stadtbezirksbudget.antrag.entities;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.io.Serial;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Vertretungsberechtigter extends Zahlungsempfaenger {
    @Serial
    private static final long serialVersionUID = 1L;

    @NotBlank private String nachname;
    @NotBlank private String vorname;
    @NotNull private String mobilNr;
}
