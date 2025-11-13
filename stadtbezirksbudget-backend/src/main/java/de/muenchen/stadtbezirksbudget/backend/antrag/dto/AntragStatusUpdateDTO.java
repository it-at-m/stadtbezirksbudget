package de.muenchen.stadtbezirksbudget.backend.antrag.dto;

import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Status;
import jakarta.validation.constraints.NotNull;

/**
 * Data Transfer Object (DTO) for updating the status of an Antrag.
 */
public record AntragStatusUpdateDTO(@NotNull Status status) {
}
