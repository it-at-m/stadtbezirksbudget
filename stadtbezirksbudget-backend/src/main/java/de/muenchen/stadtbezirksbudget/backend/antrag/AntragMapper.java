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
    @Mapping(target = "zammadNr", constant = "ZM-10011001") // TODO: Replace with actual zammadNr
    @Mapping(target = "aktenzeichen", constant = "MÜN-2023-00123") // TODO: Replace with actual aktenzeichen
    @Mapping(target = "eingangDatum", expression = "java(antrag.getEingangsdatum().atStartOfDay())") // TODO: Replace with actual date
    @Mapping(target = "antragstellerName", source = "antrag.antragsteller.name")
    @Mapping(target = "projektTitel", source = "antrag.projekt.titel")
    @Mapping(target = "beantragtesBudget", source = "antrag.finanzierung.beantragtesBudget")
    @Mapping(target = "istFehlbetrag", constant = "true") // TODO: Replace with actual Logik when geforderterZuschuss is safed in data base
    @Mapping(target = "aktualisierung", constant = "Fachanwendung") // TODO: Replace with actual application
    @Mapping(target = "aktualisierungDatum", expression = "java(LocalDateTime.now())") // TODO: Replace with actual date
    AntragSummaryDTO toAntragSummaryDTO(Antrag antrag);
}
