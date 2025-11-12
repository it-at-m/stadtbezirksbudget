package de.muenchen.stadtbezirksbudget.backend.theentity;

import de.muenchen.stadtbezirksbudget.backend.theentity.dto.TheEntityMapper;
import de.muenchen.stadtbezirksbudget.backend.theentity.dto.TheEntityRequestDTO;
import de.muenchen.stadtbezirksbudget.backend.theentity.dto.TheEntityResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

// TODO: Has to be removed as soon as UnicodeFilterConfigurationTest was refactored
@RestController
@RequiredArgsConstructor
@RequestMapping("/theEntity")
public class TheEntityController {

    private final TheEntityService theEntityService;
    private final TheEntityMapper theEntityMapper;

    /**
     * Create a new entity.
     * Creates a new entity using the provided entity details.
     *
     * @param theEntityRequestDTO the details of the entity to create
     * @return the created entity as a DTO
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TheEntityResponseDTO saveTheEntity(@Valid @RequestBody final TheEntityRequestDTO theEntityRequestDTO) {
        return theEntityMapper.toDTO(theEntityService.createTheEntity(theEntityMapper.toEntity(theEntityRequestDTO)));
    }
}
