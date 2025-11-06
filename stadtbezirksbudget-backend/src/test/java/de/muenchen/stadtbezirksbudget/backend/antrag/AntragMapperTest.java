package de.muenchen.stadtbezirksbudget.backend.antrag;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import de.muenchen.stadtbezirksbudget.backend.antrag.dto.AntragSummaryDTO;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Antrag;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Antragsteller;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Bearbeitungsstand;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Finanzierung;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Finanzierungsmittel;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Kategorie;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Projekt;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Status;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.VoraussichtlicheAusgabe;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

class AntragMapperTest {

    private final AntragMapper antragMapper = Mappers.getMapper(AntragMapper.class);

    @Nested
    class ToAntragSummaryDTO {

        Antrag antrag;

        @BeforeEach
        void setup() {
            antrag = new Antrag();
            antrag.setId(UUID.randomUUID());

            final Bearbeitungsstand bearbeitungsstand = new Bearbeitungsstand();
            bearbeitungsstand.setAnmerkungen("Keine Anmerkungen");
            bearbeitungsstand.setStatus(Status.VOLLSTAENDIG);

            antrag.setBearbeitungsstand(bearbeitungsstand);
            antrag.setEingangDatum(LocalDate.of(2025, 10, 15).atStartOfDay());

            final Projekt projekt = new Projekt();
            projekt.setTitel("Testprojekt");

            antrag.setProjekt(projekt);

            final Antragsteller antragsteller = new Antragsteller();
            antragsteller.setName("Max Mustermann");

            antrag.setAntragsteller(antragsteller);

            final Finanzierung finanzierung = new Finanzierung();
            final List<VoraussichtlicheAusgabe> ausgaben = new ArrayList<>();
            final VoraussichtlicheAusgabe ausgabe = new VoraussichtlicheAusgabe();
            ausgabe.setBetrag(BigDecimal.valueOf(100.0));
            ausgabe.setKategorie("Testkategorie");
            ausgabe.setDirektoriumNotiz("Testnotiz");
            ausgaben.add(ausgabe);
            finanzierung.setVoraussichtlicheAusgaben(ausgaben);
            final List<Finanzierungsmittel> mittelListe = new ArrayList<>();
            final Finanzierungsmittel finanzierungsmittel = new Finanzierungsmittel();
            finanzierungsmittel.setBetrag(BigDecimal.valueOf(50.0));
            finanzierungsmittel.setKategorie(Kategorie.EIGENMITTEL);
            finanzierungsmittel.setDirektoriumNotiz("Testnotiz");
            mittelListe.add(finanzierungsmittel);
            finanzierung.setFinanzierungsmittel(mittelListe);
            finanzierung.setBeantragtesBudget(BigDecimal.valueOf(50.0));

            antrag.setFinanzierung(finanzierung);
        }

        @Test
        void testGivenAntragEntityWithFehlbetragThenReturnsCorrectDTO() {
            final AntragSummaryDTO result = antragMapper.toAntragSummaryDTO(antrag);

            assertNotNull(result);
            assertThat(result.istFehlbetrag()).isTrue();
        }

        @Test
        void testGivenAntragEntityWithNoFehlbetragThenReturnsCorrectDTO() {
            antrag.getFinanzierung().setBeantragtesBudget(BigDecimal.valueOf(25.0));
            final AntragSummaryDTO result = antragMapper.toAntragSummaryDTO(antrag);

            assertNotNull(result);
            assertThat(result.istFehlbetrag()).isFalse();
        }
    }
}
