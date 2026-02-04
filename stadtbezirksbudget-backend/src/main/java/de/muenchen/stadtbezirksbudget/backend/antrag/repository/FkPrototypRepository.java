package de.muenchen.stadtbezirksbudget.backend.antrag.repository;

import de.muenchen.stadtbezirksbudget.backend.antrag.entity.FkPrototyp;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FkPrototypRepository extends JpaRepository<FkPrototyp, Long> {
}
