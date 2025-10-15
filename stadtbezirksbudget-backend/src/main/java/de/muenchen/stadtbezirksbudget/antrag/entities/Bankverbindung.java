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
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "person", "geldinstitut", "iban", "bic", "zahlungsempfaenger_id" }))
public class Bankverbindung extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull @ManyToOne
    private Zahlungsempfaenger zahlungsempfaenger;

    @NotBlank private String person;
    @NotBlank private String geldinstitut;
    @NotBlank private String iban;
    @NotBlank private String bic;
}
