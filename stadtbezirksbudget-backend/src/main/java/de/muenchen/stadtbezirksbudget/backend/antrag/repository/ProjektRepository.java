package de.muenchen.stadtbezirksbudget.backend.antrag.repository;

import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Projekt;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for managing {@link Projekt} entities.
 * <p>
 * This interface provides basic CRUD operations for {@link Projekt} objects.
 */
@Repository
public interface ProjektRepository extends CrudRepository<Projekt, UUID> {

}
