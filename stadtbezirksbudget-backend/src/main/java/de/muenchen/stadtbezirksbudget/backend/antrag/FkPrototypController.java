package de.muenchen.stadtbezirksbudget.backend.antrag;

import de.muenchen.stadtbezirksbudget.backend.antrag.dto.FkPrototypDTO;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.FkPrototyp;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/antrag")
public class FkPrototypController {
    private final FkPrototypService service;

    public FkPrototypController(final FkPrototypService service) {
        this.service = service;
    }

    @PostMapping(value = "/prototyp", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public FkPrototyp create(@RequestBody final FkPrototypDTO dto) {
        return service.create(dto);
    }
}
