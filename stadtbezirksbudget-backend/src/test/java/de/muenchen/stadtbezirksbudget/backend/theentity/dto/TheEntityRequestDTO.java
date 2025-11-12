package de.muenchen.stadtbezirksbudget.backend.theentity.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

// TODO (#317): Has to be removed as soon as UnicodeFilterConfigurationTest was refactored
public record TheEntityRequestDTO(@NotNull @Size(min = 2, max = 8) String textAttribute) {
}
