package de.muenchen.stadtbezirksbudget.backend.antrag.dto;

import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Status;

/**
 * Data Transfer Object (DTO) for updating the status of an Antrag.
 */
public record AntragStatusUpdateDTO(Status status) {
}
