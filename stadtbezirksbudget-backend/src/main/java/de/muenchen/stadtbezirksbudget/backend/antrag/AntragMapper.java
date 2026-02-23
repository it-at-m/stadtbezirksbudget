package de.muenchen.stadtbezirksbudget.backend.antrag;

import de.muenchen.stadtbezirksbudget.backend.antrag.dto.AntragSummaryDTO;
import de.muenchen.stadtbezirksbudget.backend.antrag.dto.antrag_details.AntragDetailsAllgemeinDTO;
import de.muenchen.stadtbezirksbudget.backend.antrag.dto.antrag_details.AntragDetailsDTO;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Antrag;
import de.muenchen.stadtbezirksbudget.backend.kafka.KafkaDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

/**
 * Mapper to map Antrag entity to its DTOs and vice versa.
 */
@Mapper
@SuppressWarnings("PMD.AvoidDuplicateLiterals")
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
    @Mapping(target = "bezirksausschussNr", source = "antrag.bezirksinformationen.ausschussNr")
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
    @Mapping(target = "istGegendert", source = "antrag.istGegendert")
    @Mapping(target = "anmerkungen", source = "bearbeitungsstand.anmerkungen")
    AntragDetailsAllgemeinDTO toAllgemeinDTO(Antrag antrag);

    @Mappings(
            {
                    @Mapping(target = "bezirksinformationen.ausschussNr", source = "bezirksausschussNr"),
                    @Mapping(target = "eingangDatum", expression = "java(java.time.LocalDateTime.of(2026,2,5,15,30))"),
                    @Mapping(target = "zammadTicketNr", constant = "12345"),
                    @Mapping(target = "aktualisierungDatum", expression = "java(java.time.LocalDateTime.of(2026,2,5,15,30))"),
                    @Mapping(target = "aktenzeichen", constant = "2026-00-00/01234"),
                    @Mapping(target = "aktualisierungArt", constant = "FACHANWENDUNG"),
                    @Mapping(target = "bearbeitungsstand.anmerkungen", constant = "Muster Anmerkung"),
                    @Mapping(target = "bearbeitungsstand.istMittelabruf", constant = "false"),
                    @Mapping(target = "bearbeitungsstand.status", constant = "EINGEGANGEN"),
                    @Mapping(target = "finanzierung.istPersonVorsteuerabzugsberechtigt", constant = "false"),
                    @Mapping(target = "finanzierung.istProjektVorsteuerabzugsberechtigt", constant = "false"),
                    @Mapping(target = "finanzierung.kostenAnmerkung", constant = "Muster Anmerkung"),
                    @Mapping(target = "finanzierung.begruendungEigenmittel", constant = "Muster Begründung"),
                    @Mapping(target = "finanzierung.beantragtesBudget", expression = "java(new java.math.BigDecimal(\"1500.00\"))"),
                    @Mapping(target = "finanzierung.istEinladungFoerderhinweis", constant = "true"),
                    @Mapping(target = "finanzierung.istWebsiteFoerderhinweis", constant = "true"),
                    @Mapping(target = "finanzierung.istSonstigerFoerderhinweis", constant = "true"),
                    @Mapping(target = "finanzierung.sonstigeFoerderhinweise", constant = "Muster Förderhinweis"),
                    @Mapping(
                            target = "finanzierung.voraussichtlicheAusgaben",
                            expression = "java(java.util.List.of(de.muenchen.stadtbezirksbudget.backend.antrag.entity.VoraussichtlicheAusgabe.builder().kategorie(\"Muster Kategorie\").betrag(new java.math.BigDecimal(5000)).direktoriumNotiz(\"Muster Notiz\").build()))"
                    ),
                    @Mapping(
                            target = "finanzierung.finanzierungsmittel",
                            expression = "java(java.util.List.of(de.muenchen.stadtbezirksbudget.backend.antrag.entity.Finanzierungsmittel.builder().kategorie(de.muenchen.stadtbezirksbudget.backend.antrag.entity.Kategorie.EIGENMITTEL).betrag(new java.math.BigDecimal(3000)).direktoriumNotiz(\"Muster Notiz\").build()))"
                    ),
                    @Mapping(target = "projekt.titel", constant = "Muster Projekt"),
                    @Mapping(target = "projekt.start", expression = "java(java.time.LocalDate.of(2026,5,5))"),
                    @Mapping(target = "projekt.fristBruchBegruendung", constant = "Muster Begründung"),
                    @Mapping(target = "projekt.ende", expression = "java(java.time.LocalDate.of(2026,5,5))"),
                    @Mapping(target = "projekt.beschreibung", constant = "Muster Beschreibung"),
                    @Mapping(target = "antragsteller.name", source = "nameAntragsteller"),
                    @Mapping(target = "antragsteller.email", constant = "max.mustermann@muster.de"),
                    @Mapping(target = "antragsteller.telefonNr", constant = "01234567"),
                    @Mapping(target = "antragsteller.rechtsform", constant = "NATUERLICHE_PERSON"),
                    @Mapping(target = "antragsteller.zielsetzung", constant = "Muster Zielsetzung"),
                    @Mapping(target = "antragsteller.adresse.strasseHausnummer", constant = "12"),
                    @Mapping(target = "antragsteller.adresse.ort", constant = "Musterstadt"),
                    @Mapping(target = "antragsteller.adresse.postleitzahl", constant = "12345"),
                    @Mapping(target = "antragsteller.adresse.weitereAngaben", constant = "Etage 4"),
                    @Mapping(target = "bankverbindung.istVonVertretungsberechtigtem", constant = "false"),
                    @Mapping(target = "bankverbindung.geldinstitut", source = "geldinstitut"),
                    @Mapping(target = "bankverbindung.iban", constant = "DE123456789101112131415"),
                    @Mapping(target = "bankverbindung.bic", constant = "STABUMUEXXX"),
                    @Mapping(
                            target = "andereZuwendungsantraege",
                            expression = "java(java.util.List.of(de.muenchen.stadtbezirksbudget.backend.antrag.entity.AndererZuwendungsantrag.builder().antragsdatum(java.time.LocalDate.of(2026,1,5)).stelle(\"KULT\").betrag(new java.math.BigDecimal(2000)).status(de.muenchen.stadtbezirksbudget.backend.antrag.entity.AndererZuwendungsantragStatus.BEANTRAGT).build()))"
                    )
            }
    )
    Antrag toAntrag(KafkaDTO kafkaDTO);
}
