package de.muenchen.stadtbezirksbudget.backend.antrag.entity;

import de.muenchen.stadtbezirksbudget.backend.common.BaseEntity;
import jakarta.persistence.Embeddable;
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
 * Represents a payee's bank details.
 * Contains information about the person, the financial institution, IBAN, and BIC.
 * The bank details are uniquely identified by the combination of person, financial institution,
 * IBAN, and BIC.
 */
@Entity
@Getter
@Setter
@Builder
@Embeddable
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class Bankverbindung extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 1L;

    @NotBlank private String person;
    @NotBlank private String geldinstitut;
    @NotBlank private String iban;
    @NotBlank private String bic;
}
