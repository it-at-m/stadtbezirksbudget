package de.muenchen.stadtbezirksbudget.backend.antrag;

import de.muenchen.stadtbezirksbudget.backend.antrag.dto.AntragFilterDTO;
import de.muenchen.stadtbezirksbudget.backend.antrag.dto.AntragStatusUpdateDTO;
import de.muenchen.stadtbezirksbudget.backend.antrag.dto.FilterOptionsDTO;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.AktualisierungArt;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Antrag;
import de.muenchen.stadtbezirksbudget.backend.antrag.repository.AntragRepository;
import de.muenchen.stadtbezirksbudget.backend.common.NameView;
import de.muenchen.stadtbezirksbudget.backend.common.NotFoundException;
import de.muenchen.stadtbezirksbudget.backend.common.TitelView;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
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
     * @param pageable pagination information
     * @param antragFilterDTO filter information
     * @return a page of Antrag entities
     */
    public Page<Antrag> getAntragPage(final Pageable pageable, final AntragFilterDTO antragFilterDTO) {
        log.info("Get antrag page with pageable {} and filterDTO {}", pageable, antragFilterDTO);
        return antragRepository.findAll(
                (root, query, criteriaBuilder) -> antragFilterService.filterAntrag(antragFilterDTO, root, criteriaBuilder),
                pageable);
    }

    /**
     * Retrieves a single Antrag by its id.
     *
     * @param id id of the Antrag to get
     * @return the antrag with the id
     * @throws NotFoundException if no Antrag was found for the given id
     */
    public Antrag getAntrag(final UUID id) {
        log.info("Get antrag with id {}", id);
        return antragRepository.findById(id).orElseThrow(() -> new NotFoundException(String.format("Could not find antrag with id %s", id)));
    }

    /**
     * Retrieves all Antragsteller names and Projekt titles.
     *
     * @return a FilterOptionsDTO containing lists of Antragsteller names and Projekt titles
     */
    public FilterOptionsDTO getFilterOptions() {
        log.info("Get FilterOptions");
        final List<String> antragstellerNameList = antragRepository.findDistinctByAntragsteller_nameIsNotNullOrderByAntragsteller_nameAsc().stream()
                .map(NameView::getAntragsteller_Name).toList();
        final List<String> projektTitelList = antragRepository.findDistinctByProjekt_titelIsNotNullOrderByProjekt_titelAsc().stream()
                .map(TitelView::getProjekt_Titel).toList();
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
        saveAntrag(antrag, AktualisierungArt.FACHANWENDUNG);
    }

    /**
     * Saves an Antrag entity to the database, updating its last modification date and type.
     *
     * @param antrag the Antrag entity to save
     * @param aktualisierungArt the type of update being performed
     */
    void saveAntrag(final Antrag antrag, final AktualisierungArt aktualisierungArt) {
        antrag.setAktualisierungArt(aktualisierungArt);
        antrag.setAktualisierungDatum(LocalDateTime.now());
        log.info("Save antrag with id {}", antrag.getId());
        antragRepository.save(antrag);
    }
}
