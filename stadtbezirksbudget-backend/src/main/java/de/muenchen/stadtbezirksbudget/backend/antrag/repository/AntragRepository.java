package de.muenchen.stadtbezirksbudget.backend.antrag.repository;

import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Antrag;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for managing {@link Antrag} entities.
 * <p>
 * This interface provides basic CRUD operations as well as paging and sorting
 * for {@link Antrag} objects.
 */
@Repository
public interface AntragRepository extends PagingAndSortingRepository<Antrag, UUID>, CrudRepository<Antrag, UUID> {
    /**
     * Repository interface for querying distinct Antragsteller names.
     */
    @Query("SELECT DISTINCT z.name FROM Antragsteller z")
    List<String> findDistinctAntragstellerNames();

    /**
     * Repository interface for querying distinct Projekt titles.
     */
    @Query("SELECT DISTINCT p.titel FROM Projekt p")
    List<String> findDistinctProjektTitles();
}
