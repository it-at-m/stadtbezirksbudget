package de.muenchen.stadtbezirksbudget.backend.dummy;

import de.muenchen.stadtbezirksbudget.backend.dummy.dto.DummyDTO;
import de.muenchen.stadtbezirksbudget.backend.dummy.entity.DummyEntity;
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
@RequestMapping("/dummyEntity")
public class DummyController {
    private final DummyService dummyService;
    private final DummyMapper dummyMapper;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<DummyDTO> getDummiesByPageAndSize(@PageableDefault final Pageable pageable) {
        final Page<DummyEntity> dummyEntityPage = dummyService.getAllEntities(pageable);
        final List<DummyDTO> dummyDTOList = dummyEntityPage.getContent().stream().map(dummyMapper::toDTO).toList();
        return new PageImpl<>(dummyDTOList, dummyEntityPage.getPageable(), dummyEntityPage.getTotalElements());
    }

}
