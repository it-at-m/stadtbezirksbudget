package de.muenchen.stadtbezirksbudget.backend.antrag.entity;

import static de.muenchen.stadtbezirksbudget.backend.TestConstants.SPRING_NO_SECURITY_PROFILE;
import static de.muenchen.stadtbezirksbudget.backend.TestConstants.SPRING_TEST_PROFILE;
import static org.assertj.core.api.Assertions.assertThat;

import de.muenchen.stadtbezirksbudget.backend.TestConstants;
import de.muenchen.stadtbezirksbudget.backend.antrag.repository.FinanzierungRepository;
import de.muenchen.stadtbezirksbudget.backend.antrag.repository.FinanzierungsmittelRepository;
import de.muenchen.stadtbezirksbudget.backend.antrag.repository.VoraussichtlicheAusgabeRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Transactional
@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(profiles = { SPRING_TEST_PROFILE, SPRING_NO_SECURITY_PROFILE })
@SuppressWarnings({ "PMD.ShortClassName" })
class FinanzierungTest {
    @Container
    @ServiceConnection
    @SuppressWarnings("unused")
    private static final PostgreSQLContainer<?> POSTGRE_SQL_CONTAINER = new PostgreSQLContainer<>(
            TestConstants.TESTCONTAINERS_POSTGRES_IMAGE);

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private FinanzierungRepository finanzierungRepository;
    @Autowired
    private VoraussichtlicheAusgabeRepository voraussichtlicheAusgabeRepository;
    @Autowired
    private FinanzierungsmittelRepository finanzierungsmittelRepository;

    private Finanzierung createFinanzierung(final List<BigDecimal> ausgaben, final List<BigDecimal> finanzierungen, final List<Kategorie> kategorien) {
        if (finanzierungen.size() != kategorien.size()) {
            throw new IllegalArgumentException("Kategorien und finanzierungen müssen die gleiche Anzahl von Elementen (size) haben.");
        }
        final Finanzierung finanzierung = Finanzierung.builder()
                .istProjektVorsteuerabzugsberechtigt(false)
                .kostenAnmerkung("Kosten Anmerkung")
                .begruendungEigenmittel("Begründung")
                .beantragtesBudget(BigDecimal.ZERO)
                .istEinladungFoerderhinweis(false)
                .istWebsiteFoerderhinweis(false)
                .istSonstigerFoerderhinweis(false)
                .sonstigeFoerderhinweise("")
                .bewilligterZuschuss(BigDecimal.ZERO)
                .build();

        final List<VoraussichtlicheAusgabe> voraussichtlicheAusgaben = ausgaben.stream().map(betrag -> VoraussichtlicheAusgabe.builder()
                .kategorie("Kategorie")
                .betrag(betrag)
                .direktoriumNotiz("Notiz")
                .finanzierung(finanzierung)
                .build()).toList();

        final List<Finanzierungsmittel> finanzierungsmittel = createFinanzierungsmittel(finanzierungen, finanzierung, kategorien);

        finanzierung.setVoraussichtlicheAusgaben(voraussichtlicheAusgaben);
        finanzierung.setFinanzierungsmittel(finanzierungsmittel);

        final Finanzierung savedFinanzierung = finanzierungRepository.save(finanzierung);
        voraussichtlicheAusgabeRepository.saveAll(voraussichtlicheAusgaben);
        finanzierungsmittelRepository.saveAll(finanzierungsmittel);

        entityManager.flush();
        entityManager.clear();

        return savedFinanzierung;
    }

    private List<Finanzierungsmittel> createFinanzierungsmittel(final List<BigDecimal> finanzierungen, final Finanzierung finanzierung,
            final List<Kategorie> kategorien) {
        return IntStream.range(0, finanzierungen.size())
                .mapToObj(i -> Finanzierungsmittel.builder()
                        .kategorie(kategorien.get(i))
                        .betrag(finanzierungen.get(i))
                        .direktoriumNotiz("Notiz")
                        .finanzierung(finanzierung)
                        .build())
                .toList();
    }

    @Nested
    class GesamtFormula {
        private static Arguments args(final List<BigDecimal> ausgaben, final BigDecimal expectedGesamtkosten) {
            return Arguments.of(ausgaben, expectedGesamtkosten);
        }

        private static Stream<Arguments> gesamtFormulaTestData() {
            return Stream.of(
                    args(List.of(BigDecimal.valueOf(2000.01), BigDecimal.valueOf(3000.99)), BigDecimal.valueOf(5001)),
                    args(List.of(BigDecimal.valueOf(1000.37), BigDecimal.valueOf(500)), BigDecimal.valueOf(1500.37)),
                    args(List.of(BigDecimal.valueOf(2500), BigDecimal.valueOf(750)), BigDecimal.valueOf(3250)),
                    args(List.of(BigDecimal.valueOf(2000)), BigDecimal.valueOf(2000)));
        }

        @ParameterizedTest
        @MethodSource("gesamtFormulaTestData")
        void testGesamtkostenCalculation(final List<BigDecimal> ausgaben, final BigDecimal expectedGesamtkosten) {
            final Finanzierung created = createFinanzierung(ausgaben, Collections.nCopies(ausgaben.size(), BigDecimal.ZERO),
                    Collections.nCopies(ausgaben.size(), Kategorie.EIGENMITTEL));
            final Finanzierung loaded = finanzierungRepository.findById(created.getId()).orElseThrow();
            assertThat(loaded.getGesamtkosten()).isEqualByComparingTo(expectedGesamtkosten);
        }

        @ParameterizedTest
        @MethodSource("gesamtFormulaTestData")
        void testGesamtmittelCalculation(final List<BigDecimal> finanzierungen, final BigDecimal expectedGesamtmittel) {
            final Finanzierung created = createFinanzierung(Collections.nCopies(finanzierungen.size(), BigDecimal.ZERO), finanzierungen,
                    Collections.nCopies(finanzierungen.size(), Kategorie.EIGENMITTEL));
            final Finanzierung loaded = finanzierungRepository.findById(created.getId()).orElseThrow();
            assertThat(loaded.getGesamtmittel()).isEqualByComparingTo(expectedGesamtmittel);
        }
    }

    @Nested
    class Art {
        private static Arguments args(final List<BigDecimal> ausgaben, final List<BigDecimal> finanzierungen, final FinanzierungArt art) {
            return Arguments.of(ausgaben, finanzierungen, art);
        }

        private static Stream<Arguments> artTestData() {
            return Stream.of(
                    args(List.of(BigDecimal.valueOf(5000), BigDecimal.ONE),
                            List.of(BigDecimal.ZERO),
                            FinanzierungArt.FEHL),
                    args(List.of(BigDecimal.valueOf(5000)),
                            List.of(BigDecimal.ZERO),
                            FinanzierungArt.FEST),
                    args(List.of(BigDecimal.valueOf(5000), BigDecimal.ONE),
                            List.of(BigDecimal.ZERO, BigDecimal.ONE),
                            FinanzierungArt.FEHL),
                    args(List.of(BigDecimal.valueOf(5000)),
                            List.of(BigDecimal.ONE),
                            FinanzierungArt.FEHL));
        }

        @ParameterizedTest
        @MethodSource("artTestData")
        void testArtDetermination(final List<BigDecimal> ausgaben, final List<BigDecimal> finanzierungen, final FinanzierungArt expectedArt) {
            final Finanzierung created = createFinanzierung(ausgaben, finanzierungen, Collections.nCopies(finanzierungen.size(), Kategorie.EIGENMITTEL));
            final Finanzierung loaded = finanzierungRepository.findById(created.getId()).orElseThrow();
            assertThat(loaded.getArt()).isEqualTo(expectedArt);
        }
    }

    @Nested
    class ZuwenigEigenmittel {
        private static Arguments args(final List<BigDecimal> ausgaben, final List<BigDecimal> finanzierungen, final List<Kategorie> kategorien,
                final boolean expectedResult) {
            return Arguments.of(ausgaben, finanzierungen, kategorien, expectedResult);
        }

        private static Stream<Arguments> zuwenigEigenmittelTestData() {
            return Stream.of(
                    args(List.of(BigDecimal.valueOf(2000), BigDecimal.valueOf(3000), BigDecimal.valueOf(0)),
                            List.of(BigDecimal.valueOf(1250), BigDecimal.valueOf(0), BigDecimal.valueOf(0)),
                            List.of(Kategorie.EIGENMITTEL, Kategorie.EINNAHMEN, Kategorie.ZUWENDUNGEN_DRITTER),
                            false),
                    args(List.of(BigDecimal.valueOf(2000), BigDecimal.valueOf(3000), BigDecimal.valueOf(0)),
                            List.of(BigDecimal.valueOf(1249), BigDecimal.valueOf(0), BigDecimal.valueOf(0)),
                            List.of(Kategorie.EIGENMITTEL, Kategorie.EINNAHMEN, Kategorie.ZUWENDUNGEN_DRITTER),
                            true),
                    args(List.of(BigDecimal.valueOf(2000), BigDecimal.valueOf(3000), BigDecimal.valueOf(0)),
                            List.of(BigDecimal.valueOf(0), BigDecimal.valueOf(0), BigDecimal.valueOf(0)),
                            List.of(Kategorie.EIGENMITTEL, Kategorie.EINNAHMEN, Kategorie.ZUWENDUNGEN_DRITTER),
                            true),
                    args(List.of(BigDecimal.valueOf(0), BigDecimal.valueOf(0), BigDecimal.valueOf(1)),
                            List.of(BigDecimal.valueOf(0), BigDecimal.valueOf(0), BigDecimal.valueOf(0)),
                            List.of(Kategorie.EIGENMITTEL, Kategorie.EINNAHMEN, Kategorie.ZUWENDUNGEN_DRITTER),
                            true),
                    args(List.of(BigDecimal.valueOf(2000), BigDecimal.valueOf(3000)),
                            List.of(BigDecimal.valueOf(1000), BigDecimal.valueOf(250)),
                            List.of(Kategorie.EIGENMITTEL, Kategorie.EINNAHMEN),
                            true),
                    args(List.of(BigDecimal.valueOf(2000), BigDecimal.valueOf(3000)),
                            List.of(BigDecimal.valueOf(2000), BigDecimal.valueOf(3000)),
                            List.of(Kategorie.ZUWENDUNGEN_DRITTER, Kategorie.EINNAHMEN),
                            true),
                    args(List.of(BigDecimal.valueOf(2000), BigDecimal.valueOf(3000)),
                            List.of(BigDecimal.valueOf(1500), BigDecimal.valueOf(750)),
                            List.of(Kategorie.EINNAHMEN, Kategorie.EINNAHMEN),
                            true),
                    args(List.of(BigDecimal.valueOf(2000)),
                            List.of(BigDecimal.valueOf(100)),
                            List.of(Kategorie.ZUWENDUNGEN_DRITTER),
                            true),
                    args(List.of(BigDecimal.valueOf(2000), BigDecimal.valueOf(3000)),
                            List.of(BigDecimal.valueOf(1000), BigDecimal.valueOf(500)),
                            List.of(Kategorie.ZUWENDUNGEN_DRITTER, Kategorie.EIGENMITTEL),
                            true));
        }

        @ParameterizedTest
        @MethodSource("zuwenigEigenmittelTestData")
        void testZuwenigEigenmittel(final List<BigDecimal> ausgaben, final List<BigDecimal> finanzierungen, final List<Kategorie> kategorien,
                final boolean expectedResult) {
            final Finanzierung created = createFinanzierung(ausgaben, finanzierungen, kategorien);
            final Finanzierung loaded = finanzierungRepository.findById(created.getId()).orElseThrow();
            assertThat(loaded.isIstZuwenigEigenmittel()).isEqualTo(expectedResult);
        }
    }
}
