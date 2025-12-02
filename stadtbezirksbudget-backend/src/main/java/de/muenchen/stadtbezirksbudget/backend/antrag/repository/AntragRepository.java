package de.muenchen.stadtbezirksbudget.backend.antrag.repository;

import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Antrag;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for managing {@link Antrag} entities.
 * <p>
 * This interface provides basic CRUD operations, paging and sorting
 * and specification-based querying for {@link Antrag} objects.
 */
@Repository
public interface AntragRepository extends PagingAndSortingRepository<Antrag, UUID>, CrudRepository<Antrag, UUID>, JpaSpecificationExecutor<Antrag> {
}
