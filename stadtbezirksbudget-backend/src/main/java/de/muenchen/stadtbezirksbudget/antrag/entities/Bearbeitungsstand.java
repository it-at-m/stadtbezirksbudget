package de.muenchen.stadtbezirksbudget.antrag.entities;

import de.muenchen.stadtbezirksbudget.backend.common.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import java.io.Serial;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Bearbeitungsstand extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull private String anmerkungen;
    private Status status;
    private boolean istMittelabruf;
}
