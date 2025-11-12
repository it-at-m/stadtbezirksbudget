package de.muenchen.stadtbezirksbudget.backend.theentity;

import java.util.UUID;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

// TODO: Has to be removed as soon as UnicodeFilterConfigurationTest was refactored
@Repository
public interface TheEntityRepository extends PagingAndSortingRepository<TheEntity, UUID>, CrudRepository<TheEntity, UUID> {

}
