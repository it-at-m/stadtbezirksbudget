package de.muenchen.stadtbezirksbudget.backend.antrag.repository;

import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Finanzierungsmittel;
import java.util.List;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FinanzierungsmittelRepository extends PagingAndSortingRepository<Finanzierungsmittel, UUID>, CrudRepository<Finanzierungsmittel, UUID> {
    List<Finanzierungsmittel> findByFinanzierungIdIn(List<UUID> finanzierungIds);
}
