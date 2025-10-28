package de.muenchen.stadtbezirksbudget.backend.antrag;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Antrag;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Antragsteller;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Bearbeitungsstand;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Finanzierung;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Finanzierungsmittel;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Projekt;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Status;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.VoraussichtlicheAusgabe;
import de.muenchen.stadtbezirksbudget.backend.antrag.repository.AntragRepository;
import de.muenchen.stadtbezirksbudget.backend.antrag.repository.FinanzierungsmittelRepository;
import de.muenchen.stadtbezirksbudget.backend.antrag.repository.VoraussichtlicheAusgabeRepository;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
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
class AntragSummaryServiceTest {

    @Mock
    private AntragRepository antragRepository;

    @Mock
    private FinanzierungsmittelRepository finanzierungsmittelRepository;

    @Mock
    private VoraussichtlicheAusgabeRepository voraussichtlicheAusgabeRepository;

    @InjectMocks
    private AntragSummaryService antragSummaryService;

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
        antrag.setEingangsdatum(LocalDate.now());
        antrag.setProjekt(projekt);
        antrag.setAntragsteller(antragsteller);
        antrag.setFinanzierung(finanzierung);

        return antrag;
    }

    @Nested
    class GetAllEntities {

        @Test
        void testGetAllEntities() {
            final Pageable pageable = PageRequest.of(0, 10);
            final Finanzierung finanzierung = new Finanzierung();
            finanzierung.setId(UUID.randomUUID());

            final Antragsteller antragsteller = new Antragsteller();
            antragsteller.setName("Max Mustermann");

            final Bearbeitungsstand bearbeitungsstand = new Bearbeitungsstand();
            bearbeitungsstand.setAnmerkungen("Keine Anmerkungen");
            bearbeitungsstand.setIstMittelabruf(false);

            final Antrag antrag = createAntrag(bearbeitungsstand, antragsteller, finanzierung, "Projekt Titel", "Projekt Beschreibung", Status.VOLLSTAENDIG);

            final VoraussichtlicheAusgabe ausgabe = new VoraussichtlicheAusgabe();
            ausgabe.setBetrag(100.0);
            when(voraussichtlicheAusgabeRepository.findByFinanzierungId(finanzierung.getId()))
                    .thenReturn(Collections.singletonList(ausgabe));

            final Finanzierungsmittel finanzierungsmittel = new Finanzierungsmittel();
            finanzierungsmittel.setBetrag(30.0);
            when(finanzierungsmittelRepository.findByFinanzierungId(finanzierung.getId()))
                    .thenReturn(Collections.singletonList(finanzierungsmittel));

            when(antragRepository.findAll(pageable)).thenReturn(new PageImpl<>(Collections.singletonList(antrag), pageable, 1));

            final Page<AntragSummaryDTO> result = antragSummaryService.getAllEntities(pageable);

            assertThat(result).isNotNull();
            assertThat(result.getContent()).hasSize(1);
            assertThat(result.getContent().getFirst().projekttitel()).isEqualTo("Projekt Titel");
            assertThat(result.getContent().getFirst().beantragtesBudget()).isEqualTo(70.0);
            verify(antragRepository).findAll(pageable);
            verify(voraussichtlicheAusgabeRepository).findByFinanzierungId(finanzierung.getId());
            verify(finanzierungsmittelRepository).findByFinanzierungId(finanzierung.getId());
        }

        @Test
        void testEmptyAntragList() {
            final Pageable pageable = PageRequest.of(0, 10);
            when(antragRepository.findAll(pageable)).thenReturn(new PageImpl<>(Collections.emptyList(), pageable, 0));

            final Page<AntragSummaryDTO> result = antragSummaryService.getAllEntities(pageable);

            assertThat(result).isNotNull();
            assertThat(result.getContent()).isEmpty();
            verify(antragRepository).findAll(pageable);
        }

        @Test
        void testMoreRequestsThanAvailable() {
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

            final Page<AntragSummaryDTO> result = antragSummaryService.getAllEntities(pageable);

            assertThat(result).isNotNull();
            assertThat(result.getContent()).hasSize(1);
            verify(antragRepository).findAll(pageable);
        }

        @Test
        void testMoreRequestsThanAvailableDifferentPageSize() {
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

            final Page<AntragSummaryDTO> result = antragSummaryService.getAllEntities(pageable);

            assertThat(result).isNotNull();
            assertThat(result.getContent()).hasSize(2);
            verify(antragRepository).findAll(pageable);
        }
    }
}
