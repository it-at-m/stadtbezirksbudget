package de.muenchen.stadtbezirksbudget.backend.antrag;

import de.muenchen.stadtbezirksbudget.backend.antrag.dto.AntragSummaryDTO;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Antrag;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper to map Antrag entity to its DTOs and vice versa.
 */
@Mapper
public interface AntragMapper {
    /**
     * Maps an Antrag entity to an AntragSummaryDTO.
     *
     * @param antrag the Antrag entity
     * @return the corresponding AntragSummaryDTO
     */
    @Mapping(target = "status", source = "antrag.bearbeitungsstand.status")
    @Mapping(target = "zammadNr", source = "zammadTicketNr")
    @Mapping(target = "antragstellerName", source = "antrag.antragsteller.name")
    @Mapping(target = "projektTitel", source = "antrag.projekt.titel")
    @Mapping(target = "beantragtesBudget", source = "antrag.finanzierung.beantragtesBudget")
    @Mapping(target = "istFehlbetrag", expression = "java(antrag.getFinanzierung().istFehlbetrag())")
    @Mapping(target = "aktualisierung", source = "aktualisierungArt")
    AntragSummaryDTO toAntragSummaryDTO(Antrag antrag);
}
