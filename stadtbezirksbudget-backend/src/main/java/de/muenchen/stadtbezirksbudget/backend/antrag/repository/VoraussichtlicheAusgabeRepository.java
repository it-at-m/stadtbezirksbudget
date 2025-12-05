package de.muenchen.stadtbezirksbudget.backend.antrag.repository;

import de.muenchen.stadtbezirksbudget.backend.antrag.entity.VoraussichtlicheAusgabe;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoraussichtlicheAusgabeRepository extends CrudRepository<VoraussichtlicheAusgabe, UUID> {

}
