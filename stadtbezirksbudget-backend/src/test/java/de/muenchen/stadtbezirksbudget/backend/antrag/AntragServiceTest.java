package de.muenchen.stadtbezirksbudget.backend.antrag;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import de.muenchen.stadtbezirksbudget.backend.antrag.dto.AntragStatusUpdateDTO;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Antrag;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Antragsteller;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Bearbeitungsstand;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Finanzierung;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Projekt;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Status;
import de.muenchen.stadtbezirksbudget.backend.antrag.repository.AntragRepository;
import de.muenchen.stadtbezirksbudget.backend.common.NotFoundException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class AntragServiceTest {
    private final ObjectMapper objectMapper = createObjectMapper();

    @Mock
    private AntragRepository antragRepository;
    @InjectMocks
    private AntragService antragService;

    private ObjectMapper createObjectMapper() {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.addMixIn(Finanzierung.class, FinanzierungMixIn.class);
        return mapper;
    }

    private Antrag createAntrag(final Bearbeitungsstand bearbeitungsstand, final Antragsteller antragsteller, final Finanzierung finanzierung,
            final String titel, final String beschreibung,
            final Status status) {
        bearbeitungsstand.setStatus(status);

        final Projekt projekt = new Projekt();
        projekt.setTitel(titel);
        projekt.setBeschreibung(beschreibung);
        projekt.setStart(LocalDate.now());
        projekt.setEnde(LocalDate.now().plusMonths(1));

        final Antrag antrag = new Antrag();
        antrag.setId(UUID.randomUUID());
        antrag.setBearbeitungsstand(bearbeitungsstand);
        antrag.setBezirksausschussNr(123);
        antrag.setEingangDatum(LocalDate.now().atStartOfDay());
        antrag.setProjekt(projekt);
        antrag.setAntragsteller(antragsteller);
        antrag.setFinanzierung(finanzierung);

        return antrag;
    }

    @SuppressWarnings("PMD.CommentDefaultAccessModifier")
    abstract static class FinanzierungMixIn {
        @com.fasterxml.jackson.annotation.JsonIgnore
        abstract boolean istFehlbetrag();
    }

    @Nested
    class GetAllEntities {
        @Test
        void testEmptyAntragList() {
            final Pageable pageable = PageRequest.of(0, 10);
            when(antragRepository.findAll(pageable)).thenReturn(new PageImpl<>(Collections.emptyList(), pageable, 0));

            final Page<Antrag> result = antragService.getAntragPage(pageable);

            assertThat(result).isNotNull();
            assertThat(result.getContent()).isEmpty();
            verify(antragRepository).findAll(pageable);
        }

        @Test
        void testGetAllEntitiesWithDifferentStatus() {
            final Pageable pageable = PageRequest.of(0, 10);
            final Finanzierung finanzierung = new Finanzierung();
            finanzierung.setId(UUID.randomUUID());

            final Antragsteller antragsteller = new Antragsteller();
            antragsteller.setName("Max Mustermann");

            final Bearbeitungsstand bearbeitungsstand = new Bearbeitungsstand();
            bearbeitungsstand.setAnmerkungen("Keine Anmerkungen");
            bearbeitungsstand.setIstMittelabruf(false);
            bearbeitungsstand.setStatus(Status.ABGELEHNT_VON_BA);

            final Antrag antrag = createAntrag(bearbeitungsstand, antragsteller, finanzierung, "Projekt Titel", "Projekt Beschreibung",
                    Status.ABGELEHNT_VON_BA);

            when(antragRepository.findAll(pageable)).thenReturn(new PageImpl<>(Collections.singletonList(antrag), pageable, 1));

            final Page<Antrag> result = antragService.getAntragPage(pageable);

            assertThat(result).isNotNull();
            assertThat(result.getContent()).hasSize(1);
            verify(antragRepository).findAll(pageable);
        }

        @Test
        void testGetAllEntitiesWithMultipleItems() {
            final Pageable pageable = PageRequest.of(0, 5);
            final Finanzierung finanzierung = new Finanzierung();
            finanzierung.setId(UUID.randomUUID());

            final Antragsteller antragsteller1 = new Antragsteller();
            antragsteller1.setName("Max Mustermann");

            final Antrag antrag1 = createAntrag(new Bearbeitungsstand(), antragsteller1, finanzierung, "Projekt 1", "Beschreibung 1",
                    Status.SITZUNGSVORLAGE_ERSTELLT);

            final Antragsteller antragsteller2 = new Antragsteller();
            antragsteller2.setName("Erika Mustermann");

            final Antrag antrag2 = createAntrag(new Bearbeitungsstand(), antragsteller2, finanzierung, "Projekt 2", "Beschreibung 2", Status.ABGESCHLOSSEN);

            when(antragRepository.findAll(pageable)).thenReturn(new PageImpl<>(List.of(antrag1, antrag2), pageable, 2));

            final Page<Antrag> result = antragService.getAntragPage(pageable);

            assertThat(result).isNotNull();
            assertThat(result.getContent()).hasSize(2);
            verify(antragRepository).findAll(pageable);
        }
    }

    @Nested
    class UpdateAntragStatus {
        @Test
        void testUpdateAntragStatusSuccessfully() throws Exception {
            final Bearbeitungsstand bearbeitungsstand = new Bearbeitungsstand();
            bearbeitungsstand.setStatus(Status.VOLLSTAENDIG);

            final Finanzierung finanzierung = new Finanzierung();
            final Antragsteller antragsteller = new Antragsteller();

            final Antrag antrag = createAntrag(bearbeitungsstand, antragsteller, finanzierung, "T", "B", Status.VOLLSTAENDIG);
            final UUID id = antrag.getId();

            final Antrag originalCopy = objectMapper.readValue(objectMapper.writeValueAsString(antrag), Antrag.class);

            when(antragRepository.findById(id)).thenReturn(Optional.of(antrag));

            antragService.updateAntragStatus(id, new AntragStatusUpdateDTO(Status.AUSZAHLUNG));

            verify(antragRepository).findById(id);
            verify(antragRepository).save(antrag);
            assertThat(antrag.getBearbeitungsstand().getStatus()).isEqualTo(Status.AUSZAHLUNG);
            assertThat(antrag).usingRecursiveComparison()
                    .ignoringFields("bearbeitungsstand.status")
                    .isEqualTo(originalCopy);
        }

        @Test
        void testUpdateAntragStatusNotFoundThrows() {
            final UUID randomId = UUID.randomUUID();
            when(antragRepository.findById(randomId)).thenReturn(Optional.empty());

            assertThrows(NotFoundException.class, () -> antragService.updateAntragStatus(randomId, new AntragStatusUpdateDTO(Status.AUSZAHLUNG)));
            verify(antragRepository).findById(randomId);
            verify(antragRepository, never()).save(any());
        }
    }
}
