package de.muenchen.stadtbezirksbudget.backend.antrag;

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

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/antrag")
public class AntragController {
    private final AntragSummaryService antragSummaryService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<AntragSummaryDTO> getAntragsdatenSubsetByPageAndSize(@PageableDefault final Pageable pageable) {
        return antragSummaryService.getAllEntities(pageable);
    }
}
