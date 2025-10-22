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
                getRandomWords("Lorem ipsum dolor sit amet consetetur"),
                getRandomWords("Lorem ipsum dolor sit amet consetetur sadipscing elitr sed diam"),
                secureRandom.nextInt(20_000),
                getRandomWords("Lorem ipsum dolor sit amet consetetur sadipscing elitr sed diam nonumy eirmod tempor invidunt"),
                getRandomWords("Lorem ipsum dolor sit amet consetetur sadipscing elitr sed diam"));
    }

    private String getRandomWords(String text) {
        String[] words = text.split(" ");
        int numberOfWords = secureRandom.nextInt(words.length - 1)+1;
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < numberOfWords; i++) {
            result.append(words[i]).append(" ");
        }
        return result.toString().trim();
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
