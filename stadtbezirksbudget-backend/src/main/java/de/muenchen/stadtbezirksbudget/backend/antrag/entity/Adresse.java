package de.muenchen.stadtbezirksbudget.backend.antrag.entity;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
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
@Getter
@Setter
@Builder
@Embeddable
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class Adresse implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @NotBlank private String strasseHausnummer;
    @NotBlank private String ort;
    @NotBlank private String postleitzahl;
    @NotNull private String weitereAngaben;
}
