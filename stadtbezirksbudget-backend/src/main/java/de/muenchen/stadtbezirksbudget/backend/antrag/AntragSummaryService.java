package de.muenchen.stadtbezirksbudget.backend.antrag;

import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Antrag;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Antragsteller;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Finanzierung;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Finanzierungsmittel;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.VoraussichtlicheAusgabe;
import de.muenchen.stadtbezirksbudget.backend.antrag.repository.AntragRepository;
import de.muenchen.stadtbezirksbudget.backend.antrag.repository.FinanzierungsmittelRepository;
import de.muenchen.stadtbezirksbudget.backend.antrag.repository.VoraussichtlicheAusgabeRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AntragSummaryService {
    private final AntragRepository antragRepository;
    private final FinanzierungsmittelRepository finanzierungsmittelRepository;
    private final VoraussichtlicheAusgabeRepository voraussichtlicheAusgabeRepository;

    public Page<AntragSummaryDTO> getAllEntities(final Pageable pageable) {
        final Page<Antrag> antragPage = antragRepository.findAll(pageable);
        if (antragPage.isEmpty()) {
            return new PageImpl<>(List.of(), pageable, 0);
        }
        final List<AntragSummaryDTO> list = antragPage.getContent().stream().map(antrag -> {
            final Finanzierung finanzierung = antrag.getFinanzierung();
            final double beantragtesBudget = voraussichtlicheAusgabeRepository.findByFinanzierungId(finanzierung.getId()).stream()
                    .mapToDouble(VoraussichtlicheAusgabe::getBetrag)
                    .sum()
                    - finanzierungsmittelRepository.findByFinanzierungId(finanzierung.getId()).stream()
                            .mapToDouble(Finanzierungsmittel::getBetrag)
                            .sum();
            return new AntragSummaryDTO(
                    antrag.getId(),
                    antrag.getBearbeitungsstand().getStatus(),
                    "ZM-10011001",
                    antrag.getBezirksausschussNr(),
                    convertToLocalDateTime(antrag.getEingangsdatum()),
                    antrag.getProjekt().getTitel(),
                    antrag.getAntragsteller().getName(),
                    beantragtesBudget,
                    "Fachanwendung",
                    convertToLocalDateTime(antrag.getEingangsdatum()),
                    antrag.getBearbeitungsstand().getAnmerkungen(),
                    "Admin");
        }).collect(Collectors.toList());
        return new PageImpl<>(list, pageable, antragPage.getTotalElements());
    }

    private LocalDateTime convertToLocalDateTime(final LocalDate localDate) {
        return localDate.atStartOfDay();
    }
}
