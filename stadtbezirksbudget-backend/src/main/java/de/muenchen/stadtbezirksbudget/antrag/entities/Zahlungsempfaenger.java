package de.muenchen.stadtbezirksbudget.antrag.entities;

import de.muenchen.stadtbezirksbudget.backend.common.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.io.Serial;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(
        uniqueConstraints = @UniqueConstraint(
                columnNames = { "dtype", "telefon_nr", "email", "adresse_id", "nachname", "vorname", "mobil_nr", "name", "zielsetzung", "rechtsform" }
        )
)
public abstract class Zahlungsempfaenger extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 1L;

    @NotBlank private String telefonNr;
    @NotBlank private String email;
    @NotNull @ManyToOne
    private Adresse adresse;

    protected Zahlungsempfaenger() {
        super();
    }
}
