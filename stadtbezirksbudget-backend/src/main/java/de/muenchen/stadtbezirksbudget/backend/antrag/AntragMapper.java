package de.muenchen.stadtbezirksbudget.backend.antrag;

import de.muenchen.stadtbezirksbudget.backend.antrag.dto.AntragSummaryDTO;
import de.muenchen.stadtbezirksbudget.backend.antrag.dto.antrag_details.AntragDetailsAllgemeinDTO;
import de.muenchen.stadtbezirksbudget.backend.antrag.dto.antrag_details.AntragDetailsDTO;
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
    @Mapping(target = "finanzierungArt", source = "antrag.finanzierung.art")
    @Mapping(target = "aktualisierung", source = "aktualisierungArt")
    AntragSummaryDTO toAntragSummaryDTO(Antrag antrag);

    /**
     * Maps an Antrag entity to an AntragDetailsDTO.
     *
     * @param antrag the Antrag entity
     * @return the corresponding AntragDetailsDTO
     */
    @Mapping(target = "allgemein", source = ".")
    AntragDetailsDTO toDetailsDTO(Antrag antrag);

    /**
     * Maps an Antrag entity to an AntragDetailsAllgemeinDTO.
     *
     * @param antrag the Antrag entity
     * @return the corresponding AntragDetailsAllgemeinDTO
     */
    @Mapping(target = "projektTitel", source = "antrag.projekt.titel")
    @Mapping(target = "antragstellerName", source = "antrag.antragsteller.name")
    @Mapping(target = "beantragtesBudget", source = "antrag.finanzierung.beantragtesBudget")
    @Mapping(target = "status", source = "antrag.bearbeitungsstand.status")
    @Mapping(target = "rubrik", source = "antrag.projekt.rubrik")
    @Mapping(target = "zammadNr", source = "zammadTicketNr")
    @Mapping(target = "istGegendert", source = "antrag.verwendungsnachweis.istGegendert")
    @Mapping(target = "anmerkungen", source = "bearbeitungsstand.anmerkungen")
    AntragDetailsAllgemeinDTO toAllgemeinDTO(Antrag antrag);
}
