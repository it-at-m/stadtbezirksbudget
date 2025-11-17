package de.muenchen.stadtbezirksbudget.backend.antrag;

import de.muenchen.stadtbezirksbudget.backend.antrag.dto.AntragStatusUpdateDTO;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Antrag;
import de.muenchen.stadtbezirksbudget.backend.antrag.repository.AntragRepository;
import de.muenchen.stadtbezirksbudget.backend.common.NotFoundException;
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

    /**
     * Retrieves a paginated list of Antrag entities.
     *
     * @param pageable pagination information
     * @return a page of Antrag entities
     */
    public Page<Antrag> getAntragPage(final Pageable pageable) {
        log.info("Get antrag page with page number {} and page size {}", pageable.getPageNumber(), pageable.getPageSize());
        return antragRepository.findAll(pageable);
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
