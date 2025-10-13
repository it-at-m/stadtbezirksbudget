package de.muenchen.stadtbezirksbudget.antrag.entities;

import de.muenchen.stadtbezirksbudget.common.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "titel", "beschreibung", "start", "ende" }))
public class Projekt extends BaseEntity {
    @NotBlank private String titel;
    @NotBlank private String beschreibung;
    @NotEmpty private Date start;
    @NotEmpty private Date ende;
}
