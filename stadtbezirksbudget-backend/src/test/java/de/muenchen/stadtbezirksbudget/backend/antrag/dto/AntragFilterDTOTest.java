package de.muenchen.stadtbezirksbudget.backend.antrag.dto;

import static org.assertj.core.api.Assertions.assertThat;

import de.muenchen.stadtbezirksbudget.backend.antrag.entity.AktualisierungArt;
import de.muenchen.stadtbezirksbudget.backend.antrag.entity.Status;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class AntragFilterDTOTest {
    @Nested
    class AntragFilterDTOConstructor {
        @Test
        void testAntragFilterDTOInitialization() {
            final List<Status> statusList = List.of(Status.ABGELEHNT_VON_BA);
            final List<Integer> bezirksausschussNrList = List.of(1, 2, 3);
            final LocalDateTime eingangDatumVon = LocalDateTime.now().minusDays(1);
            final LocalDateTime eingangDatumBis = LocalDateTime.now();
            final String antragstellerName = "Max Mustermann";
            final String projektTitel = "Projekt Titel";
            final BigDecimal beantragtesBudgetVon = BigDecimal.valueOf(1000);
            final BigDecimal beantragtesBudgetBis = BigDecimal.valueOf(5000);
            final Boolean istFehlbetrag = true;
            final List<AktualisierungArt> aktualisierungArtList = List.of(AktualisierungArt.E_AKTE);
            final LocalDateTime aktualisierungDatumVon = LocalDateTime.now().minusDays(2);
            final LocalDateTime aktualisierungDatumBis = LocalDateTime.now();

            final AntragFilterDTO antragFilterDTO = new AntragFilterDTO(
                    statusList,
                    bezirksausschussNrList,
                    eingangDatumVon,
                    eingangDatumBis,
                    antragstellerName,
                    projektTitel,
                    beantragtesBudgetVon,
                    beantragtesBudgetBis,
                    istFehlbetrag,
                    aktualisierungArtList,
                    aktualisierungDatumVon,
                    aktualisierungDatumBis);

            assertThat(antragFilterDTO.status()).isEqualTo(statusList);
            assertThat(antragFilterDTO.bezirksausschussNr()).isEqualTo(bezirksausschussNrList);
            assertThat(antragFilterDTO.eingangDatumVon()).isEqualTo(eingangDatumVon);
            assertThat(antragFilterDTO.eingangDatumBis()).isEqualTo(eingangDatumBis);
            assertThat(antragFilterDTO.antragstellerName()).isEqualTo(antragstellerName);
            assertThat(antragFilterDTO.projektTitel()).isEqualTo(projektTitel);
            assertThat(antragFilterDTO.beantragtesBudgetVon()).isEqualTo(beantragtesBudgetVon);
            assertThat(antragFilterDTO.beantragtesBudgetBis()).isEqualTo(beantragtesBudgetBis);
            assertThat(antragFilterDTO.istFehlbetrag()).isEqualTo(istFehlbetrag);
            assertThat(antragFilterDTO.aktualisierungArt()).isEqualTo(aktualisierungArtList);
            assertThat(antragFilterDTO.aktualisierungDatumVon()).isEqualTo(aktualisierungDatumVon);
            assertThat(antragFilterDTO.aktualisierungDatumBis()).isEqualTo(aktualisierungDatumBis);
        }

        @Test
        void testAntragFilterDTOInitializationWithNullLists() {
            final AntragFilterDTO antragFilterDTO = new AntragFilterDTO(
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null);

            assertThat(antragFilterDTO.status()).isEmpty();
            assertThat(antragFilterDTO.bezirksausschussNr()).isEmpty();
            assertThat(antragFilterDTO.aktualisierungArt()).isEmpty();
        }
    }
}
