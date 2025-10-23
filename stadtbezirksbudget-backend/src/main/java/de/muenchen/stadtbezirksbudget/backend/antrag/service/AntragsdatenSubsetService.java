package de.muenchen.stadtbezirksbudget.backend.antrag.service;

import de.muenchen.stadtbezirksbudget.backend.antrag.dto.AntragsdatenSubsetDTO;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Antrag;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Antragsteller;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Finanzierung;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Finanzierungsmittel;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.VoraussichtlicheAusgabe;
import de.muenchen.stadtbezirksbudget.backend.antrag.repository.AntragRepository;
import de.muenchen.stadtbezirksbudget.backend.antrag.repository.FinanzierungsmittelRepository;
import de.muenchen.stadtbezirksbudget.backend.antrag.repository.VoraussichtlicheAusgabeRepository;
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
public class AntragsdatenSubsetService {
    private final AntragRepository antragRepository;
    private final FinanzierungsmittelRepository finanzierungsmittelRepository;
    private final VoraussichtlicheAusgabeRepository voraussichtlicheAusgabeRepository;

    public Page<AntragsdatenSubsetDTO> getAllEntities(final Pageable pageable) {
        final List<Antrag> antragList = antragRepository.findAll(pageable).stream().toList();
        final List<AntragsdatenSubsetDTO> list = antragList.stream().map(antrag -> {
            final Finanzierung finanzierung = antrag.getFinanzierung();
            final double beantragtesBudget = voraussichtlicheAusgabeRepository.findByFinanzierungId(finanzierung.getId()).stream()
                    .mapToDouble(VoraussichtlicheAusgabe::getBetrag)
                    .sum()
                    - finanzierungsmittelRepository.findByFinanzierungId(finanzierung.getId()).stream()
                            .mapToDouble(Finanzierungsmittel::getBetrag)
                            .sum();
            return new AntragsdatenSubsetDTO(
                    antrag.getId(),
                    antrag.getBearbeitungsstand().getStatus(),
                    antrag.getBezirksausschussNr(),
                    antrag.getEingangsdatum().toString(),
                    antrag.getProjekt().getTitel(),
                    ((Antragsteller) antrag.getAntragsteller()).getName(),
                    beantragtesBudget,
                    antrag.getBearbeitungsstand().getAnmerkungen(),
                    "Admin");
        }).collect(Collectors.toList());
        final int start = (int) pageable.getOffset();
        final int end = Math.min(start + pageable.getPageSize(), list.size());
        final List<AntragsdatenSubsetDTO> subList = list.subList(start, end);
        return new PageImpl<>(subList, pageable, list.size());
    }
}
