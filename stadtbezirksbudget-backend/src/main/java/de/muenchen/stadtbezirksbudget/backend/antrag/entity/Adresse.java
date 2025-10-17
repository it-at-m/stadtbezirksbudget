package de.muenchen.stadtbezirksbudget.backend.antrag.entity;

import de.muenchen.stadtbezirksbudget.backend.common.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import java.io.Serial;
import lombok.Getter;
import lombok.Setter;

/**
 * Repräsentiert eine Adresse mit Straße, Hausnummer, Ort und Postleitzahl.
 * Die Adresse ist eindeutig durch die Kombination aus Straße, Hausnummer, Ort und Postleitzahl.
 */
@Entity
@Getter
@Setter
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "strasse", "hausnummer", "ort", "postleitzahl" }))
public class Adresse extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 1L;

    @NotBlank private String strasse;
    @NotBlank private String hausnummer;
    @NotBlank private String ort;
    @NotBlank private String postleitzahl;
}
