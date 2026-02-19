package de.muenchen.stadtbezirksbudget.backend.antrag.dto;

import de.muenchen.stadtbezirksbudget.backend.common.ExcludedFromGeneratedCoverage;

/**
 * Data Transfer Object (DTO) for updating the references of an Antrag.
 */
@ExcludedFromGeneratedCoverage(reason = "DTO is a pure data carrier (no logic) and therefore unreasonable to test.")
public record AntragReferenceUpdateDTO(
        String eakteCooAdresse) {
}
