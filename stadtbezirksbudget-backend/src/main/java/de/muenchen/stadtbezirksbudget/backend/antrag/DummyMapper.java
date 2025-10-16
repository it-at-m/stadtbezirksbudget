package de.muenchen.stadtbezirksbudget.backend.antrag;

import de.muenchen.stadtbezirksbudget.backend.antrag.dto.DummyDTO;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.DummyEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface DummyMapper {
    @Mapping(expression = "java(dummy.getWeight() > 50)", target = "isOverweight")
    DummyDTO toDTO(DummyEntity dummy);
}
