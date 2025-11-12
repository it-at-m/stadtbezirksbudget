package de.muenchen.stadtbezirksbudget.backend.theentity;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

// TODO (#317): Has to be removed as soon as UnicodeFilterConfigurationTest was refactored
@Service
@Slf4j
@RequiredArgsConstructor
public class TheEntityService {

    private final TheEntityRepository theEntityRepository;

    public TheEntity createTheEntity(final TheEntity entity) {
        log.debug("Create TheEntity {}", entity);
        return theEntityRepository.save(entity);
    }
}
