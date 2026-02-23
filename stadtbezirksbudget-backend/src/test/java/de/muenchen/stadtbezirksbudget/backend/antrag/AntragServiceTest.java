package de.muenchen.stadtbezirksbudget.backend.antrag;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import de.muenchen.stadtbezirksbudget.backend.antrag.dto.AntragFilterDTO;
import de.muenchen.stadtbezirksbudget.backend.antrag.dto.AntragReferenceUpdateDTO;
import de.muenchen.stadtbezirksbudget.backend.antrag.dto.AntragStatusUpdateDTO;
import de.muenchen.stadtbezirksbudget.backend.antrag.dto.FilterOptionsDTO;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.AktualisierungArt;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Antrag;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Antragsteller;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Bearbeitungsstand;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Finanzierung;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Projekt;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Status;
import de.muenchen.stadtbezirksbudget.backend.antrag.repository.AntragRepository;
import de.muenchen.stadtbezirksbudget.backend.common.NameView;
import de.muenchen.stadtbezirksbudget.backend.common.NotFoundException;
import de.muenchen.stadtbezirksbudget.backend.common.TitelView;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
class AntragServiceTest {
    private final ObjectMapper objectMapper = createObjectMapper();

    @Mock
    private AntragRepository antragRepository;

    @Spy
    @InjectMocks
    private AntragService antragService;

    private ObjectMapper createObjectMapper() {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
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

    @Nested
    class GetAllEntities {
        @Test
        void testEmptyAntragList() {
            final Pageable pageable = PageRequest.of(0, 10);
            final AntragFilterDTO antragFilterDTO = mock(AntragFilterDTO.class);

            when(antragRepository.findAll(any(Specification.class), any(Pageable.class)))
                    .thenReturn(new PageImpl<>(Collections.emptyList(), pageable, 0));

            final Page<Antrag> result = antragService.getAntragPage(pageable, antragFilterDTO);

            assertThat(result).isNotNull();
            assertThat(result.getContent()).isEmpty();
            verify(antragRepository).findAll(any(Specification.class), any(Pageable.class));
        }

        @Test
        void testGetAllEntitiesWithDifferentStatus() {
            final Pageable pageable = PageRequest.of(0, 10);
            final AntragFilterDTO antragFilterDTO = mock(AntragFilterDTO.class);
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

            when(antragRepository.findAll(any(Specification.class), any(Pageable.class)))
                    .thenReturn(new PageImpl<>(Collections.singletonList(antrag), pageable, 1));

            final Page<Antrag> result = antragService.getAntragPage(pageable, antragFilterDTO);

            assertThat(result).isNotNull();
            assertThat(result.getContent()).hasSize(1);
            verify(antragRepository).findAll(any(Specification.class), any(Pageable.class));
        }

        @Test
        void testGetAllEntitiesWithMultipleItems() {
            final Pageable pageable = PageRequest.of(0, 5);
            final AntragFilterDTO antragFilterDTO = mock(AntragFilterDTO.class);
            final Finanzierung finanzierung = new Finanzierung();
            finanzierung.setId(UUID.randomUUID());

            final Antragsteller antragsteller1 = new Antragsteller();
            antragsteller1.setName("Max Mustermann");

            final Antrag antrag1 = createAntrag(new Bearbeitungsstand(), antragsteller1, finanzierung, "Projekt 1", "Beschreibung 1",
                    Status.SITZUNGSVORLAGE_ERSTELLT);

            final Antragsteller antragsteller2 = new Antragsteller();
            antragsteller2.setName("Erika Mustermann");

            final Antrag antrag2 = createAntrag(new Bearbeitungsstand(), antragsteller2, finanzierung, "Projekt 2", "Beschreibung 2", Status.ABGESCHLOSSEN);

            when(antragRepository.findAll(any(Specification.class), any(Pageable.class)))
                    .thenReturn(new PageImpl<>(List.of(antrag1, antrag2), pageable, 2));

            final Page<Antrag> result = antragService.getAntragPage(pageable, antragFilterDTO);

            assertThat(result).isNotNull();
            assertThat(result.getContent()).hasSize(2);
            verify(antragRepository).findAll(any(Specification.class), any(Pageable.class));
        }
    }

    @Nested
    class GetAntrag {
        @Test
        void testGetAntrag() {
            final Antrag antrag = createAntrag(new Bearbeitungsstand(), new Antragsteller(), new Finanzierung(), "T", "B", Status.VOLLSTAENDIG);
            final UUID id = antrag.getId();

            when(antragRepository.findById(id)).thenReturn(Optional.of(antrag));

            final Antrag receivedAntrag = antragService.getAntrag(id);

            verify(antragRepository).findById(id);
            assertThat(receivedAntrag).isEqualTo(antrag);
        }

        @Test
        void testGetAntragNotExisting() {
            final UUID randomId = UUID.randomUUID();
            when(antragRepository.findById(randomId)).thenReturn(Optional.empty());

            assertThrows(NotFoundException.class, () -> antragService.getAntrag(randomId));
            verify(antragRepository).findById(randomId);
        }
    }

    @Nested
    class GetFilterOptions {
        @Test
        void testEmptyLists() {
            when(antragRepository.findDistinctByAntragsteller_nameIsNotNullOrderByAntragsteller_nameAsc()).thenReturn(Collections.emptyList());
            when(antragRepository.findDistinctByProjekt_titelIsNotNullOrderByProjekt_titelAsc()).thenReturn(Collections.emptyList());

            final FilterOptionsDTO filterOptions = antragService.getFilterOptions();

            assertThat(filterOptions).isNotNull();
            assertThat(filterOptions.antragstellerNamen()).isEmpty();
            assertThat(filterOptions.projektTitel()).isEmpty();
        }

        @Test
        void testNonEmptyLists() {
            final List<String> antragstellerNames = List.of("Antragsteller1", "Antragsteller2");
            final List<String> projektTitles = List.of("Projekt1", "Projekt2");

            final List<NameView> nameViews = antragstellerNames.stream().map(name -> (NameView) () -> name).toList();
            final List<TitelView> titelViews = projektTitles.stream().map(titel -> (TitelView) () -> titel).toList();

            when(antragRepository.findDistinctByAntragsteller_nameIsNotNullOrderByAntragsteller_nameAsc()).thenReturn(nameViews);
            when(antragRepository.findDistinctByProjekt_titelIsNotNullOrderByProjekt_titelAsc()).thenReturn(titelViews);

            final FilterOptionsDTO filterOptions = antragService.getFilterOptions();

            assertThat(filterOptions).isNotNull();
            assertThat(filterOptions.antragstellerNamen()).containsExactlyElementsOf(antragstellerNames);
            assertThat(filterOptions.projektTitel()).containsExactlyElementsOf(projektTitles);
        }

        @Test
        void testPartialEmptyLists() {
            final List<String> antragstellerNames = List.of("Antragsteller1");

            final List<NameView> nameViews = antragstellerNames.stream().map(name -> (NameView) () -> name).toList();
            final List<TitelView> titelViews = Collections.emptyList();

            when(antragRepository.findDistinctByAntragsteller_nameIsNotNullOrderByAntragsteller_nameAsc()).thenReturn(nameViews);
            when(antragRepository.findDistinctByProjekt_titelIsNotNullOrderByProjekt_titelAsc()).thenReturn(titelViews);

            final FilterOptionsDTO filterOptions = antragService.getFilterOptions();

            assertThat(filterOptions).isNotNull();
            assertThat(filterOptions.antragstellerNamen()).containsExactlyElementsOf(antragstellerNames);
            assertThat(filterOptions.projektTitel()).isEmpty();
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
            verify(antragService).saveAntrag(eq(antrag), eq(AktualisierungArt.FACHANWENDUNG));
            assertThat(antrag.getBearbeitungsstand().getStatus()).isEqualTo(Status.AUSZAHLUNG);
            assertThat(antrag).usingRecursiveComparison()
                    .ignoringFields("bearbeitungsstand.status")
                    .ignoringFields("aktualisierungDatum")
                    .ignoringFields("aktualisierungArt")
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

    @Nested
    class UpdateAntragReference {
        @Test
        void testUpdateReferenceNotFoundThrows() {
            final UUID randomId = UUID.randomUUID();
            when(antragRepository.findById(randomId)).thenReturn(Optional.empty());

            assertThrows(NotFoundException.class, () -> antragService.updateAntragReference(
                    randomId, new AntragReferenceUpdateDTO("COO.6804.7915.3.3210800")));
            verify(antragRepository).findById(randomId);
            verify(antragRepository, never()).save(any());
        }

        @Test
        void testUpdateReferenceCooAdresse() throws Exception {
            final Antrag antrag = createAntrag(new Bearbeitungsstand(), new Antragsteller(), new Finanzierung(), "T", "B", Status.VOLLSTAENDIG);
            antrag.setEakteCooAdresse("COO.6804.7915.3.3210800");
            final UUID id = antrag.getId();

            final Antrag originalCopy = objectMapper.readValue(objectMapper.writeValueAsString(antrag), Antrag.class);

            when(antragRepository.findById(id)).thenReturn(Optional.of(antrag));

            antragService.updateAntragReference(id, new AntragReferenceUpdateDTO("COO.6804.7915.3.3210877"));
            verify(antragRepository).findById(id);
            verify(antragRepository).save(antrag);
            verify(antragService).saveAntrag(eq(antrag), eq(AktualisierungArt.FACHANWENDUNG));
            assertThat(antrag.getEakteCooAdresse()).isEqualTo("COO.6804.7915.3.3210877");
            assertThat(antrag).usingRecursiveComparison()
                    .ignoringFields("eakteCooAdresse")
                    .ignoringFields("aktualisierungDatum")
                    .ignoringFields("aktualisierungArt")
                    .isEqualTo(originalCopy);
        }
    }

    @Nested
    class SaveAntrag {
        @ParameterizedTest
        @EnumSource(AktualisierungArt.class)
        void testSaveAntrag(final AktualisierungArt aktualisierungArt) throws Exception {
            final Antrag antrag = createAntrag(new Bearbeitungsstand(), new Antragsteller(), new Finanzierung(), "T", "B", Status.VOLLSTAENDIG);
            final Antrag originalCopy = objectMapper.readValue(objectMapper.writeValueAsString(antrag), Antrag.class);

            final LocalDateTime before = LocalDateTime.now();
            antragService.saveAntrag(antrag, aktualisierungArt);
            final LocalDateTime after = LocalDateTime.now();

            assertThat(antrag.getAktualisierungArt()).isEqualTo(aktualisierungArt);
            assertThat(antrag.getAktualisierungDatum()).isAfterOrEqualTo(before).isBeforeOrEqualTo(after);

            assertThat(antrag).usingRecursiveComparison()
                    .ignoringFields("aktualisierungDatum")
                    .ignoringFields("aktualisierungArt")
                    .isEqualTo(originalCopy);
        }
    }
}
