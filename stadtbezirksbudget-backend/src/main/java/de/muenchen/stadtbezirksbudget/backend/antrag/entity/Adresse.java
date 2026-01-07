package de.muenchen.stadtbezirksbudget.backend.antrag.entity;

import de.muenchen.stadtbezirksbudget.backend.common.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import java.io.Serial;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents an address with street, house number, city, and postal code.
 * The address is unique based on the combination of street, house number, city, and postal code.
 */
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class Adresse extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 1L;

    @NotBlank private String strasse;
    @NotBlank private String hausnummer;
    @NotBlank private String ort;
    @NotBlank private String postleitzahl;
}
