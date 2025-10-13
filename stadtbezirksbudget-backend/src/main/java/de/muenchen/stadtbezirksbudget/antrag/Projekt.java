package de.muenchen.stadtbezirksbudget.antrag;

import de.muenchen.stadtbezirksbudget.common.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Projekt extends BaseEntity {
    @NotBlank private String titel;
    @NotBlank private String beschreibung;
    @NotEmpty private Date start;
    @NotEmpty private Date ende;
}
