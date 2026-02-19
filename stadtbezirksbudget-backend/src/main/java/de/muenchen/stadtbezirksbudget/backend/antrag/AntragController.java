package de.muenchen.stadtbezirksbudget.backend.antrag;

import de.muenchen.stadtbezirksbudget.backend.antrag.dto.AntragFilterDTO;
import de.muenchen.stadtbezirksbudget.backend.antrag.dto.AntragReferenceUpdateDTO;
import de.muenchen.stadtbezirksbudget.backend.antrag.dto.AntragStatusUpdateDTO;
import de.muenchen.stadtbezirksbudget.backend.antrag.dto.AntragSummaryDTO;
import de.muenchen.stadtbezirksbudget.backend.antrag.dto.FilterOptionsDTO;
import de.muenchen.stadtbezirksbudget.backend.antrag.dto.antrag_details.AntragDetailsDTO;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Antrag;
import de.muenchen.stadtbezirksbudget.backend.security.Authorities;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller to handle requests related to Antrag entities.
 */
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/antrag")
public class AntragController {
    private static final int UNPAGED_SIZE = -1;

    private final AntragService antragService;
    private final AntragMapper antragMapper;
    private final AntragSortMapper antragSortMapper;

    /**
     * Retrieves a paginated list of Antrag summaries.
     * Can be sorted by selected fields of Antrag.
     *
     * @param page number of the page
     * @param size amount of items in each page
     * @param sortBy external sort key defined in {@link AntragSortMapper}
     * @param sortDirection direction of sorting (ASC or DESC)
     * @param antragFilterDTO contains filter data
     * @return a page of AntragSummaryDTOs
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize(Authorities.ANTRAG_GET_SUMMARY)
    public Page<AntragSummaryDTO> getAntragSummaryPage(@RequestParam(defaultValue = "0") final int page, @RequestParam(defaultValue = "10") final int size,
            @RequestParam(required = false) final String sortBy, @RequestParam(defaultValue = "ASC") final Sort.Direction sortDirection,
            final AntragFilterDTO antragFilterDTO) {
        final Sort sort = antragSortMapper.map(sortBy, sortDirection);
        final Pageable pageable = (size == UNPAGED_SIZE) ? Pageable.unpaged(sort) : PageRequest.of(page, size, sort);
        final Page<Antrag> antragPage = antragService.getAntragPage(pageable, antragFilterDTO);
        final List<AntragSummaryDTO> summaryList = antragPage.getContent().stream()
                .map(antragMapper::toAntragSummaryDTO)
                .toList();
        return new PageImpl<>(summaryList, antragPage.getPageable(), antragPage.getTotalElements());
    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize(Authorities.ANTRAG_GET_DETAILS)
    public AntragDetailsDTO getDetails(@PathVariable final UUID id) {
        return antragMapper.toDetailsDTO(antragService.getAntrag(id));
    }

    /**
     * Retrieves all Antragsteller names and Projekt titles.
     *
     * @return a FilterOptionsDTO containing lists of Antragsteller names and Projekt titles
     */
    @GetMapping("filterOptions")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize(Authorities.ANTRAG_GET_SUMMARY)
    public FilterOptionsDTO getFilterOptions() {
        return antragService.getFilterOptions();
    }

    /**
     * Updates the status of an Antrag.
     *
     * @param id the ID of the Antrag to update
     * @param statusUpdateDTO the DTO containing the new status
     */
    @PatchMapping("{id}/status")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize(Authorities.ANTRAG_UPDATE_STATUS)
    public void updateAntragStatus(@PathVariable final UUID id, @RequestBody @Valid final AntragStatusUpdateDTO statusUpdateDTO) {
        antragService.updateAntragStatus(id, statusUpdateDTO);
    }

    /**
     * Updates the references of an Antrag.
     *
     * @param id the ID of the Antrag to update
     * @param referenceUpdateDTO the DTO containing the new references
     * @return ResponseEntity with appropriate status
     */
    @PatchMapping("{id}/reference")
    @PreAuthorize(Authorities.ANTRAG_UPDATE_REFERENCES)
    public ResponseEntity<Void> updateAntragReferences(@PathVariable final UUID id, @RequestBody @Valid final AntragReferenceUpdateDTO referenceUpdateDTO) {
        return antragService.updateAntragReference(id, referenceUpdateDTO)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
    }
}
