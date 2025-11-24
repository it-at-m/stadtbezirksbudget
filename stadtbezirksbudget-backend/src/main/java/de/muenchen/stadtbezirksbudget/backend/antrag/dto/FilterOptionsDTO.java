package de.muenchen.stadtbezirksbudget.backend.antrag.dto;

import java.util.List;

/**
 * Data Transfer Object (DTO) for filtering options.
 *
 * This DTO encapsulates lists of distinct Antragsteller names and Projekt titles,
 * which can be used for filtering purposes.
 */
public record FilterOptionsDTO(List<String> antragstellerNameList, List<String> projektTitelList) {
}
