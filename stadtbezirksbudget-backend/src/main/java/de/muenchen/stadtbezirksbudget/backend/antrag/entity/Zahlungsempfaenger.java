package de.muenchen.stadtbezirksbudget.backend.antrag.entity;

import de.muenchen.stadtbezirksbudget.backend.common.BaseEntity;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.io.Serial;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Abstract base class for payees that contains basic information
 * such as phone number, email, and address. This class serves as a
 * superclass for specific types of payees, such as applicants
 * and authorized representatives.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
@Table(
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "UniqueVertretungsberechtigter",
                        columnNames = { "dtype", "telefonNr", "email", "adresse_id", "nachname", "vorname", "mobilNr" }
                ),
                @UniqueConstraint(
                        name = "UniqueAntragsteller", columnNames = { "dtype", "telefonNr", "email", "adresse_id", "name", "zielsetzung", "rechtsform" }
                ) }
)
public abstract class Zahlungsempfaenger extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 1L;

    @NotBlank private String telefonNr;
    @NotBlank private String email;

    @NotNull @ManyToOne
    private Adresse adresse;
}
