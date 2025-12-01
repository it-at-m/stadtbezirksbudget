package de.muenchen.stadtbezirksbudget.backend.antrag.repository;

import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Zahlungsempfaenger;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for managing {@link Zahlungsempfaenger} entities.
 * <p>
 * This interface provides basic CRUD operations for {@link Zahlungsempfaenger} objects.
 */
@Repository
public interface ZahlungsempfaengerRepository extends CrudRepository<Zahlungsempfaenger, UUID> {
    /**
     * Retrieves all distinct Antragsteller names, sorted alphabetically.
     */
    @Query("SELECT DISTINCT z.name FROM Antragsteller z ORDER BY z.name ASC")
    List<String> findDistinctAntragstellerNames();

}
