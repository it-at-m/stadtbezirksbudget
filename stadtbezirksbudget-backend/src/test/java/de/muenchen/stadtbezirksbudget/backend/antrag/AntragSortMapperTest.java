package de.muenchen.stadtbezirksbudget.backend.antrag;

import static de.muenchen.stadtbezirksbudget.backend.TestConstants.SPRING_NO_SECURITY_PROFILE;
import static de.muenchen.stadtbezirksbudget.backend.TestConstants.SPRING_TEST_PROFILE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import de.muenchen.stadtbezirksbudget.backend.IntegrationTestConfiguration;
import de.muenchen.stadtbezirksbudget.backend.common.InvalidSortPropertyException;
import java.util.Objects;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
@ActiveProfiles(profiles = { SPRING_TEST_PROFILE, SPRING_NO_SECURITY_PROFILE })
@Import(IntegrationTestConfiguration.class)
class AntragSortMapperTest {
    @Autowired
    private AntragSortMapper antragSortMapper;

    private static Stream<Arguments> mappingData() {
        return Stream.of(
                Arguments.of("status", "bearbeitungsstand.status"),
                Arguments.of("zammadNr", "zammadTicketNr"),
                Arguments.of("aktenzeichen", "aktenzeichen"),
                Arguments.of("bezirksausschussNr", "bezirksinformationen.ausschussNr"),
                Arguments.of("eingangDatum", "eingangDatum"),
                Arguments.of("antragstellerName", "antragsteller.name"),
                Arguments.of("projektTitel", "projekt.titel"),
                Arguments.of("beantragtesBudget", "finanzierung.beantragtesBudget"),
                Arguments.of("finanzierungArt", "finanzierung.art"),
                Arguments.of("aktualisierung", "aktualisierungArt"),
                Arguments.of("aktualisierungDatum", "aktualisierungDatum"));
    }

    private static Stream<Arguments> invalidData() {
        return Stream.of(
                Arguments.of((Object) null),
                Arguments.of(""),
                Arguments.of("invalidField"));
    }

    @ParameterizedTest
    @MethodSource("mappingData")
    void testMappingAsc(final String sortBy, final String expectedField) {
        final Sort sort = antragSortMapper.map(sortBy, Sort.Direction.ASC);
        assertEquals(expectedField, Objects.requireNonNull(sort.getOrderFor(expectedField)).getProperty());
        assertEquals(Sort.Direction.ASC, Objects.requireNonNull(sort.getOrderFor(expectedField)).getDirection());
    }

    @ParameterizedTest
    @MethodSource("mappingData")
    void testMappingDesc(final String sortBy, final String expectedField) {
        final Sort sort = antragSortMapper.map(sortBy, Sort.Direction.DESC);
        assertEquals(expectedField, Objects.requireNonNull(sort.getOrderFor(expectedField)).getProperty());
        assertEquals(Sort.Direction.DESC, Objects.requireNonNull(sort.getOrderFor(expectedField)).getDirection());
    }

    @ParameterizedTest
    @MethodSource("invalidData")
    void testInvalidMapping(final String sortBy) {
        if (Objects.isNull(sortBy) || sortBy.isBlank()) {
            assertEquals(Sort.unsorted(), antragSortMapper.map("", Sort.Direction.ASC));
        } else {
            assertThrows(InvalidSortPropertyException.class, () -> antragSortMapper.map(sortBy, Sort.Direction.ASC));
        }
    }
}
