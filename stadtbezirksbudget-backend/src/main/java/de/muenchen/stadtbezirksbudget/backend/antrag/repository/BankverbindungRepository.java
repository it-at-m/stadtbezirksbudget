package de.muenchen.stadtbezirksbudget.backend.antrag.repository;

import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Bankverbindung;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for managing {@link Bankverbindung} entities.
 * <p>
 * This interface provides basic CRUD operations for {@link Bankverbindung} objects.
 */
@Repository
public interface BankverbindungRepository extends CrudRepository<Bankverbindung, UUID> {

}
