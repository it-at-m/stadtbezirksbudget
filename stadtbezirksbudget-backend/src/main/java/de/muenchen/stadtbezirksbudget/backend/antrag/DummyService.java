package de.muenchen.stadtbezirksbudget.backend.antrag;

import de.muenchen.stadtbezirksbudget.backend.antrag.entity.DummyEntity;
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
public class DummyService {

    private final DummyMapper dummyMapper;

    public DummyEntity getDummy() {
        final DummyEntity dummy = new DummyEntity();
        dummy.setId(UUID.randomUUID());
        dummy.setName("Dummy");
        dummy.setAge(25);
        dummy.setEmail("dummy@email.com");
        dummy.setWeight(Math.random() * 101);
        return dummy;
    }

    public Page<DummyEntity> getAllEntities(final Pageable pageable) {
        final List<DummyEntity> list = Stream.generate(this::getDummy).limit(50).toList();
        final int start = (int) pageable.getOffset();
        final int end = Math.min(start + pageable.getPageSize(), list.size());
        final List<DummyEntity> subList = list.subList(start, end);
        return new PageImpl<>(subList, pageable, list.size());
    }
}
