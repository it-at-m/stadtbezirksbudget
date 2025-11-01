package de.muenchen.stadtbezirksbudget.backend.antrag.repository;

import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Adresse;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for managing {@link Adresse} entities.
 * <p>
 * This interface provides basic CRUD operations for {@link Adresse} objects.
 */
@Repository
public interface AdresseRepository extends CrudRepository<Adresse, UUID> {

}
