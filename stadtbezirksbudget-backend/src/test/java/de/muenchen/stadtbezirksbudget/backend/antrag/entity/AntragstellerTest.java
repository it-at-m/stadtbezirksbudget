package de.muenchen.stadtbezirksbudget.backend.antrag.entity;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@SuppressWarnings("PMD.AvoidDuplicateLiterals")
class AntragstellerTest {
    @Nested
    class GetFullName {
        @Test
        void testConcatinatesWithFirstAndLastName() {
            final Antragsteller antragsteller = new Antragsteller();
            antragsteller.setVorname("John");
            antragsteller.setName("Doe");
            assertThat(antragsteller.getFullName()).isEqualTo("John Doe");
        }

        @Test
        void testOnlyNameWithBlankFirstName() {
            final Antragsteller antragsteller = new Antragsteller();
            antragsteller.setVorname(" ");
            antragsteller.setName("Doe");
            assertThat(antragsteller.getFullName()).isEqualTo("Doe");
        }

        @Test
        void testOnlyNameWithNullFirstName() {
            final Antragsteller antragsteller = new Antragsteller();
            antragsteller.setVorname(null);
            antragsteller.setName("Doe");
            assertThat(antragsteller.getFullName()).isEqualTo("Doe");
        }
    }
}
