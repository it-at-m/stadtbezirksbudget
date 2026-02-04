package de.muenchen.stadtbezirksbudget.backend.antrag;

import de.muenchen.stadtbezirksbudget.backend.antrag.dto.FkPrototypDTO;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.FkPrototyp;
import de.muenchen.stadtbezirksbudget.backend.antrag.repository.FkPrototypRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FkPrototypService {
    private final FkPrototypRepository repo;

    public FkPrototypService(final FkPrototypRepository repo) {
        this.repo = repo;
    }

    @Transactional
    public FkPrototyp create(final FkPrototypDTO dto) {
        final FkPrototyp fkPrototyp = new FkPrototyp();
        fkPrototyp.setNameAntragsteller(dto.getNameAntragsteller());
        fkPrototyp.setGeldinstitut(dto.getGeldinstitut());
        fkPrototyp.setBezirksausschussNr(dto.getBezirksausschussNr());
        return repo.save(fkPrototyp);
    }
}
