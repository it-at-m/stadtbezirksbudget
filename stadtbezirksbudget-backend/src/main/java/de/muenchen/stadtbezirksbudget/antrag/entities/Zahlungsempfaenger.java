package de.muenchen.stadtbezirksbudget.antrag.entities;

import de.muenchen.stadtbezirksbudget.common.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public abstract class Zahlungsempfaenger extends BaseEntity {
    @NotBlank private String telefonNr;
    @NotBlank private String email;
    @NotNull @ManyToOne
    private Adresse adresse;
}
