package de.muenchen.stadtbezirksbudget.antrag.entities;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Antragsteller extends Zahlungsempfaenger {
    @NotBlank private String name;
    @NotNull private String zielsetzung;
    @NotEmpty private Rechtsform rechtsform;
}
