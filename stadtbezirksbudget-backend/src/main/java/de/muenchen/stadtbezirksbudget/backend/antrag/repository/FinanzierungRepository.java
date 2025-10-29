package de.muenchen.stadtbezirksbudget.backend.antrag.repository;

import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Finanzierung;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for managing {@link Finanzierung} entities.
 * <p>
 * This interface provides basic CRUD operations for {@link Finanzierung} objects.
 */
@Repository
public interface FinanzierungRepository extends CrudRepository<Finanzierung, UUID> {

}
