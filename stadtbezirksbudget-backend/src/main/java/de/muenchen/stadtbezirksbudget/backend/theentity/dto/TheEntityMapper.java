package de.muenchen.stadtbezirksbudget.backend.theentity.dto;

import de.muenchen.stadtbezirksbudget.backend.theentity.TheEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface TheEntityMapper {

    TheEntityResponseDTO toDTO(TheEntity theEntity);

    @Mapping(target = "id", ignore = true)
    TheEntity toEntity(TheEntityRequestDTO theEntityRequestDTO);
}
