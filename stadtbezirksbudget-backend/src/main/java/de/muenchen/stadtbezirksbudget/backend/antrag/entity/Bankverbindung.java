package de.muenchen.stadtbezirksbudget.backend.antrag.entity;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import java.io.Serial;
import java.io.Serializable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents a payee's bank details.
 * Contains information about the person, the financial institution, IBAN, and BIC.
 * IBAN, and BIC.
 */
@Getter
@Setter
@Builder
@Embeddable
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class Bankverbindung implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @NotBlank private String person;
    @NotBlank private String geldinstitut;
    @NotBlank private String iban;
    @NotBlank private String bic;
}
