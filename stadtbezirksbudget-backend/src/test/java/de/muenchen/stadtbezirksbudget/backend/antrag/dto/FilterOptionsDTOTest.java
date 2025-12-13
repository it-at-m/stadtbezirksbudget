package de.muenchen.stadtbezirksbudget.backend.antrag.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class FilterOptionsDTOTest {
    @Nested
    class FilterOptionsDTOConstructor {
        @Test
        void testFilterOptionsDTOInitialization() {
            final List<String> antragstellerNamen = List.of("Max Mustermann", "Erika Mustermann");
            final List<String> projektTitel = List.of("Projekt 1", "Projekt 2");

            final FilterOptionsDTO filterOptionsDTO = new FilterOptionsDTO(antragstellerNamen, projektTitel);

            assertThat(filterOptionsDTO.antragstellerNamen()).isEqualTo(antragstellerNamen);
            assertThat(filterOptionsDTO.projektTitel()).isEqualTo(projektTitel);
        }

        @Test
        void testFilterOptionsDTOInitializationWithNullLists() {
            final FilterOptionsDTO filterOptionsDTO = new FilterOptionsDTO(null, null);

            assertThat(filterOptionsDTO.antragstellerNamen()).isEmpty();
            assertThat(filterOptionsDTO.projektTitel()).isEmpty();
        }
    }
}
