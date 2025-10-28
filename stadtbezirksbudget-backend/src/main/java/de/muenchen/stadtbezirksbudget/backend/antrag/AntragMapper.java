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
    @Mapping(target = "status", source = "antrag.bearbeitungsstand.status")
    @Mapping(target = "zammadNr", constant = "ZM-10011001")
    @Mapping(target = "eingangDatum", expression = "java(antrag.getEingangsdatum().atStartOfDay())")
    @Mapping(target = "projektTitel", source = "antrag.projekt.titel")
    @Mapping(target = "antragstellerName", source = "antrag.antragsteller.name")
    @Mapping(
            target = "beantragtesBudget",
            expression =
                    "java(antrag.getFinanzierung().getBeantragtesBudget())"
    )
    @Mapping(target = "aktualisierung", constant = "Fachanwendung")
    @Mapping(target = "aktualisierungDatum", expression = "java(LocalDateTime.now())")
    @Mapping(target = "anmerkungen", source = "antrag.bearbeitungsstand.anmerkungen")
    @Mapping(target = "bearbeiter", constant = "Max Mustermann")
    AntragSummaryDTO toAntragSummaryDTO(Antrag antrag);
}
