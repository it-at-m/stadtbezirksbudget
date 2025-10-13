package de.muenchen.stadtbezirksbudget.antrag;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Vertretungsberechtigter extends Zahlungsempfaenger {
    @NotBlank private String nachname;
    @NotBlank private String vorname;
    @NotNull private String mobilNr;
}
