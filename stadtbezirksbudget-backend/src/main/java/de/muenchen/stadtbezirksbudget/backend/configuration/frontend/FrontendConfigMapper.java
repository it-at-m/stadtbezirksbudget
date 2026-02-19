package de.muenchen.stadtbezirksbudget.backend.configuration.frontend;

import org.mapstruct.Mapper;

/**
 * Mapper to map FrontendConfigProperties to its DTOs and vice versa.
 */
@Mapper
public interface FrontendConfigMapper {
    FrontendConfigDTO toDTO(FrontendConfigProperties properties);
}
