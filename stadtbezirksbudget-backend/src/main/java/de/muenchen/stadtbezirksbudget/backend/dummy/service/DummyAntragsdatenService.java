package de.muenchen.stadtbezirksbudget.backend.dummy.service;

import de.muenchen.stadtbezirksbudget.backend.antrag.dto.AntragsdatenSubsetDTO;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Status;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class DummyAntragsdatenService {

    private final SecureRandom secureRandom = new SecureRandom();

    private AntragsdatenSubsetDTO generateAntragsdaten() {
        return new AntragsdatenSubsetDTO(UUID.randomUUID(), getRandomStatus(), secureRandom.nextInt(25), getRandomDate(),
                "Projekt: " + secureRandom.nextInt(2_000), "AdminAG",
                secureRandom.nextInt(20_000),
                "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua.",
                "admin");
    }

    private Status getRandomStatus() {
        return Status.values()[secureRandom.nextInt(Status.values().length)];
    }

    private LocalDate getRandomDate() {
        return LocalDate.ofEpochDay(secureRandom.nextInt(10_000));
    }

    public Page<AntragsdatenSubsetDTO> getAllEntities(final Pageable pageable) {
        final List<AntragsdatenSubsetDTO> list = Stream.generate(this::generateAntragsdaten).limit(50).toList();
        final int start = (int) pageable.getOffset();
        final int end = Math.min(start + pageable.getPageSize(), list.size());
        final List<AntragsdatenSubsetDTO> subList = list.subList(start, end);
        return new PageImpl<>(subList, pageable, list.size());
    }
}
