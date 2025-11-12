package de.muenchen.stadtbezirksbudget.backend.theentity.dto;

import java.util.UUID;

// TODO: Has to be removed as soon as UnicodeFilterConfigurationTest was refactored
public record TheEntityResponseDTO(UUID id, String textAttribute) {
}
