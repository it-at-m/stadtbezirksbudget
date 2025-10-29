package de.muenchen.stadtbezirksbudget.backend.antrag.repository;

import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Bearbeitungsstand;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for managing {@link Bearbeitungsstand} entities.
 * <p>
 * This interface provides basic CRUD operations for {@link Bearbeitungsstand} objects.
 */
@Repository
public interface BearbeitungsstandRepository extends CrudRepository<Bearbeitungsstand, UUID> {

}
