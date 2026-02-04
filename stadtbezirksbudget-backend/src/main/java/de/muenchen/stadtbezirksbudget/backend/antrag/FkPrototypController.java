package de.muenchen.stadtbezirksbudget.backend.antrag;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import de.muenchen.stadtbezirksbudget.backend.antrag.dto.FkPrototypDTO;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.FkPrototyp;

@RestController
@RequestMapping("/antrag")
public class FkPrototypController {
    private final FkPrototypService service;

    public FkPrototypController(FkPrototypService service) {
        this.service = service;
    }

    @PostMapping(value = "/prototyp", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public FkPrototyp create(@RequestBody FkPrototypDTO dto) {
        return service.create(dto);
    }
}
