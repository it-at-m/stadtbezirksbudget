package de.muenchen.stadtbezirksbudget.backend.antrag.repository;

import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Bearbeitungsstand;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BearbeitungsstandRepository extends PagingAndSortingRepository<Bearbeitungsstand, UUID>, CrudRepository<Bearbeitungsstand, UUID> {

}
