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
                columnNames = { "telefon_nr", "email", "adresse_id", "nachname", "vorname", "mobil_nr" }
        )
)
public class Vertretungsberechtigter extends Zahlungsempfaenger {
    @Serial
    private static final long serialVersionUID = 1L;

    private String nachname;
    private String vorname;
    private String mobilNr;
}
