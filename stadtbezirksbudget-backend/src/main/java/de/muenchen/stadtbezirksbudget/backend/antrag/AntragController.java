package de.muenchen.stadtbezirksbudget.backend.antrag;

import de.muenchen.stadtbezirksbudget.backend.antrag.dto.AntragSummaryDTO;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Antrag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
    private final AntragService antragService;
    private final AntragMapper antragMapper;

    /**
     * Retrieves a paginated list of Antrag summaries.
     *
     * @param pageable pagination information
     * @return a page of AntragSummaryDTOs
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<AntragSummaryDTO> getAntragSummaryPage(@PageableDefault final Pageable pageable) {
        final Page<Antrag> antragPage = antragService.getAntragPage(pageable);
        final List<AntragSummaryDTO> summaryList = antragPage.getContent().stream()
                .map(antragMapper::toAntragSummaryDTO)
                .toList();
        return new PageImpl<>(summaryList, pageable, antragPage.getTotalElements());
    }
}
