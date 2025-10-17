package de.muenchen.stadtbezirksbudget.backend.antrag.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import java.io.Serial;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Antragsteller extends Zahlungsempfaenger {
    @Serial
    private static final long serialVersionUID = 1L;

    @NotBlank private String name;

    @NotBlank private String zielsetzung;

    @Enumerated(EnumType.STRING)
    private Rechtsform rechtsform;
}
