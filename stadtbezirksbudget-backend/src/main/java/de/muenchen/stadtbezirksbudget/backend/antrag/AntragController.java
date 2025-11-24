package de.muenchen.stadtbezirksbudget.backend.antrag;

import de.muenchen.stadtbezirksbudget.backend.antrag.dto.AntragStatusUpdateDTO;
import de.muenchen.stadtbezirksbudget.backend.antrag.dto.AntragSummaryDTO;
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
import org.springframework.http.HttpStatus;
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

    /**
     * Retrieves a paginated list of Antrag summaries.
     *
     * @param page number of the page
     * @param size amount of items in each page
     * @return a page of AntragSummaryDTOs
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize(Authorities.ANTRAG_GET_SUMMARY)
    public Page<AntragSummaryDTO> getAntragSummaryPage(@RequestParam(defaultValue = "0") final int page, @RequestParam(defaultValue = "10") final int size) {
        final Pageable pageable = (size == UNPAGED_SIZE) ? Pageable.unpaged() : PageRequest.of(page, size);
        final Page<Antrag> antragPage = antragService.getAntragPage(pageable);
        final List<AntragSummaryDTO> summaryList = antragPage.getContent().stream()
                .map(antragMapper::toAntragSummaryDTO)
                .toList();
        return new PageImpl<>(summaryList, antragPage.getPageable(), antragPage.getTotalElements());
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
}
