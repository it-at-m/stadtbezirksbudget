package de.muenchen.stadtbezirksbudget.backend.antrag.entity;

import de.muenchen.stadtbezirksbudget.backend.common.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import java.io.Serial;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents the processing status of an application, including comments and status.
 */
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Bearbeitungsstand extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull private String anmerkungen;

    private boolean istMittelabruf;

    @NotNull @Enumerated(EnumType.STRING)
    private Status status;
}
