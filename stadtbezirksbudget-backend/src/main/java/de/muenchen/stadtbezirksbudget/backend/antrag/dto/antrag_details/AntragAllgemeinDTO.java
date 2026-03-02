package de.muenchen.stadtbezirksbudget.backend.antrag.dto.antrag_details;

import de.muenchen.stadtbezirksbudget.backend.antrag.entity.BeschlussStatus;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Status;
import de.muenchen.stadtbezirksbudget.backend.common.ExcludedFromGeneratedCoverage;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Data Transfer Object (DTO) representing the common information of an Antrag.
 */
@ExcludedFromGeneratedCoverage(reason = "DTO is a pure data carrier (no logic) and therefore unreasonable to test.")
public record AntragAllgemeinDTO(
        LocalDateTime eingangDatum,
        Status status,
        String zammadTicketNr,
        String aktenzeichen,
        String eakteCooAdresse,
        boolean istGegendert,
        String anmerkungen,
        BeschlussStatus beschlussStatus,
        boolean istZuwendungDritterBeantragt,
        BigDecimal summeAndereZuwendungsantraege,
        List<AndererZuwendungsantragDTO> andereZuwendungsantraege) {
    public AntragAllgemeinDTO {
        andereZuwendungsantraege = andereZuwendungsantraege != null ? List.copyOf(andereZuwendungsantraege) : List.of();
    }
}
