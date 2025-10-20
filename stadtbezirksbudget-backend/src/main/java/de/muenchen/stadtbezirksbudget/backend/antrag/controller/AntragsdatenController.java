package de.muenchen.stadtbezirksbudget.backend.antrag.controller;

import de.muenchen.stadtbezirksbudget.backend.antrag.dto.AntragsdatenSubsetDTO;
import de.muenchen.stadtbezirksbudget.backend.dummy.service.DummyAntragsdatenService;
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
@RequestMapping("/antragsdatenSubset")
public class AntragsdatenController {
    private final DummyAntragsdatenService dummyService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<AntragsdatenSubsetDTO> getAntragsdatenSubsetByPageAndSize(@PageableDefault final Pageable pageable) {
        final Page<AntragsdatenSubsetDTO> antragsdatenSubsetPage = dummyService.getAllEntities(pageable);
        final List<AntragsdatenSubsetDTO> antragsdatenSubsetList = antragsdatenSubsetPage.getContent().stream().toList();
        return new PageImpl<>(antragsdatenSubsetList, antragsdatenSubsetPage.getPageable(), antragsdatenSubsetPage.getTotalElements());
    }
}
