package de.muenchen.stadtbezirksbudget.backend.configuration.frontend;

import de.muenchen.stadtbezirksbudget.backend.common.ExcludedFromGeneratedCoverage;

/**
 * Data Transfer Object (DTO) for frontend configuration properties.
 *
 * @param zammadBaseUrl the base URL for Zammad
 * @param eakteBaseUrl the base URL for Eakte
 */
@ExcludedFromGeneratedCoverage(reason = "This class is a simple DTO and does not contain any logic that needs to be tested.")
public record FrontendConfigDTO(
        String zammadBaseUrl,
        String eakteBaseUrl) {
}

