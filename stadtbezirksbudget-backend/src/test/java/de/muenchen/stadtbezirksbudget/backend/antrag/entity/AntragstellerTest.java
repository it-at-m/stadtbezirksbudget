package de.muenchen.stadtbezirksbudget.backend.antrag.entity;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class AntragstellerTest {
    @Nested
    class GetFullName {
        @Test
        public void testConcatinatesWithFirstAndLastName() {
            Antragsteller antragsteller = new Antragsteller();
            antragsteller.setVorname("John");
            antragsteller.setName("Doe");
            assertThat(antragsteller.getFullName()).isEqualTo("John Doe");
        }

        @Test
        public void testOnlyNameWithBlankFirstName() {
            Antragsteller antragsteller = new Antragsteller();
            antragsteller.setVorname(" ");
            antragsteller.setName("Doe");
            assertThat(antragsteller.getFullName()).isEqualTo("Doe");
        }

        @Test
        public void testOnlyNameWithNullFirstName() {
            Antragsteller antragsteller = new Antragsteller();
            antragsteller.setVorname(null);
            antragsteller.setName("Doe");
            assertThat(antragsteller.getFullName()).isEqualTo("Doe");
        }
    }
}
