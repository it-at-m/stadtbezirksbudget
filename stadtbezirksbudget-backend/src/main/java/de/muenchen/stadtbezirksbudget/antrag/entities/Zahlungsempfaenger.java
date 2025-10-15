package de.muenchen.stadtbezirksbudget.antrag.entities;

import de.muenchen.stadtbezirksbudget.backend.common.BaseEntity;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import java.io.Serial;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
public abstract class Zahlungsempfaenger extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 1L;

    @NotBlank private String telefonNr;
    @NotBlank private String email;
    @NotBlank @ManyToOne
    private Adresse adresse;

    protected Zahlungsempfaenger() {
        super();
    }
}
