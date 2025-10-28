package de.muenchen.stadtbezirksbudget.backend.antrag.repository;

import de.muenchen.stadtbezirksbudget.backend.antrag.entity.AndererZuwendungsantrag;
import java.util.List;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AndererZuwendungsantragRepository
        extends PagingAndSortingRepository<AndererZuwendungsantrag, UUID>, CrudRepository<AndererZuwendungsantrag, UUID> {
    List<AndererZuwendungsantrag> findByAntragId(UUID antragId);
}
