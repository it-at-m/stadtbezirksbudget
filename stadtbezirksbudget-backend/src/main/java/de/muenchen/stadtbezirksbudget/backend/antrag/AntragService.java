package de.muenchen.stadtbezirksbudget.backend.antrag;

import de.muenchen.stadtbezirksbudget.backend.antrag.dto.AntragFilterDTO;
import de.muenchen.stadtbezirksbudget.backend.antrag.dto.AntragStatusUpdateDTO;
import de.muenchen.stadtbezirksbudget.backend.antrag.dto.FilterOptionsDTO;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Antrag;
import de.muenchen.stadtbezirksbudget.backend.antrag.repository.AntragRepository;
import de.muenchen.stadtbezirksbudget.backend.common.NotFoundException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service to handle business logic related to Antrag entities.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class AntragService {
    private final AntragRepository antragRepository;
    private final AntragFilterService antragFilterService;

    /**
     * Retrieves a paginated filtered list of Antrag entities.
     *
     * @param antragFilterDTO filter information
     * @param pageable pagination information
     * @return a page of Antrag entities
     */
    public Page<Antrag> getAntragPage(final Pageable pageable, final AntragFilterDTO antragFilterDTO) {
        log.info("Get antrag page with pageable {} and filterDTO {}", pageable, antragFilterDTO);
        return antragRepository.findAll(
                (root, query, criteriaBuilder) -> antragFilterService.filterAntrag(antragFilterDTO, root, criteriaBuilder),
                pageable);
    }

    /**
     * Retrieves all Antragsteller names and Projekt titles.
     *
     * @return a FilterOptionsDTO containing lists of Antragsteller names and Projekt titles
     */
    public FilterOptionsDTO getFilterOptions() {
        log.info("Get FilterOptions");
        final List<String> antragstellerNameList = antragRepository.findDistinctAntragstellerNames()
                .stream()
                .sorted()
                .collect(Collectors.toList());
        final List<String> projektTitelList = antragRepository.findDistinctProjektTitles()
                .stream()
                .sorted()
                .collect(Collectors.toList());
        return new FilterOptionsDTO(antragstellerNameList, projektTitelList);
    }

    /**
     * Updates the status of an Antrag.
     *
     * @param id the ID of the Antrag to update
     * @param statusUpdateDTO the DTO containing the new status
     */
    public void updateAntragStatus(final UUID id, final AntragStatusUpdateDTO statusUpdateDTO) {
        log.info("Update status of antrag with id {} to {}", id, statusUpdateDTO.status());
        final Antrag antrag = antragRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Antrag with id " + id + " not found"));
        antrag.getBearbeitungsstand().setStatus(statusUpdateDTO.status());
        antragRepository.save(antrag);
    }
}
