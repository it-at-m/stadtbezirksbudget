package de.muenchen.stadtbezirksbudget.antrag.entities;

import de.muenchen.stadtbezirksbudget.common.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Bearbeitungsstand extends BaseEntity {
    @NotNull private String anmerkungen;
    Status status;
    private boolean istMittelabruf;
}
