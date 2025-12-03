package de.muenchen.stadtbezirksbudget.backend.antrag.repository;

import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Antragsteller;
import de.muenchen.stadtbezirksbudget.backend.common.NameView;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface AntragstellerRepository extends CrudRepository<Antragsteller, UUID>, JpaRepository<Antragsteller, UUID> {
    /**
     * Retrieves all distinct Antragsteller names as List of NameView
     */
    List<NameView> findDistinctByNameIsNotNull();
}
