package de.muenchen.stadtbezirksbudget.antrag.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.io.Serial;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(
        uniqueConstraints = @UniqueConstraint(
                columnNames = { "telefon_nr", "email", "adresse_id", "name", "zielsetzung", "rechtsform" }
        )
)
public class Antragsteller extends Zahlungsempfaenger {
    @Serial
    private static final long serialVersionUID = 1L;

    private String name;
    private String zielsetzung;
    private Rechtsform rechtsform;
}
