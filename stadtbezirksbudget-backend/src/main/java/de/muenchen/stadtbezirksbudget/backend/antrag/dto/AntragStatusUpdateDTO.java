package de.muenchen.stadtbezirksbudget.backend.antrag.dto;

import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Status;
import de.muenchen.stadtbezirksbudget.backend.common.ExcludedFromGeneratedCoverage;
import jakarta.validation.constraints.NotNull;

/**
 * Data Transfer Object (DTO) for updating the status of an Antrag.
 */
@ExcludedFromGeneratedCoverage(reason = "DTO is a pure data carrier (no logic) and therefore unreasonable to test.")
public record AntragStatusUpdateDTO(@NotNull Status status) {
}
