package de.muenchen.stadtbezirksbudget.backend.antrag;

import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Antrag;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Finanzierungsmittel;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.VoraussichtlicheAusgabe;
import de.muenchen.stadtbezirksbudget.backend.antrag.repository.AntragRepository;
import de.muenchen.stadtbezirksbudget.backend.antrag.repository.FinanzierungsmittelRepository;
import de.muenchen.stadtbezirksbudget.backend.antrag.repository.VoraussichtlicheAusgabeRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
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

        final List<UUID> finanzierungIds = antragPage.getContent().stream()
                .map(antrag -> antrag.getFinanzierung().getId())
                .collect(Collectors.toList());

        final Map<UUID, List<VoraussichtlicheAusgabe>> ausgabenMap = voraussichtlicheAusgabeRepository
                .findByFinanzierungIdIn(finanzierungIds).stream()
                .collect(Collectors.groupingBy(va -> va.getFinanzierung().getId()));

        final Map<UUID, List<Finanzierungsmittel>> mittelMap = finanzierungsmittelRepository
                .findByFinanzierungIdIn(finanzierungIds).stream()
                .collect(Collectors.groupingBy(fm -> fm.getFinanzierung().getId()));

        final List<AntragSummaryDTO> list = antragPage.getContent().stream().map(antrag -> {
            final UUID finanzierungId = antrag.getFinanzierung().getId();
            final double ausgaben = ausgabenMap.getOrDefault(finanzierungId, List.of()).stream()
                    .mapToDouble(VoraussichtlicheAusgabe::getBetrag)
                    .sum();
            final double mittel = mittelMap.getOrDefault(finanzierungId, List.of()).stream()
                    .mapToDouble(Finanzierungsmittel::getBetrag)
                    .sum();
            final double beantragtesBudget = ausgaben - mittel;

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
