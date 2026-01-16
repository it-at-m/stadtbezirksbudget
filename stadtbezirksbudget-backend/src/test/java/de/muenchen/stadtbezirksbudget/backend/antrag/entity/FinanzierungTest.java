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
import java.util.List;
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

    private Finanzierung createFinanzierung(final List<BigDecimal> ausgaben, final List<BigDecimal> finanzierungen) {
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

        final List<Finanzierungsmittel> finanzierungsmittel = finanzierungen.stream().map(betrag -> Finanzierungsmittel.builder()
                .kategorie(Kategorie.EIGENMITTEL)
                .betrag(betrag)
                .direktoriumNotiz("Notiz")
                .finanzierung(finanzierung)
                .build()).toList();

        finanzierung.setVoraussichtlicheAusgaben(voraussichtlicheAusgaben);
        finanzierung.setFinanzierungsmittel(finanzierungsmittel);

        final Finanzierung savedFinanzierung = finanzierungRepository.save(finanzierung);
        voraussichtlicheAusgabeRepository.saveAll(voraussichtlicheAusgaben);
        finanzierungsmittelRepository.saveAll(finanzierungsmittel);

        entityManager.flush();
        entityManager.clear();

        return savedFinanzierung;
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
            final Finanzierung created = createFinanzierung(ausgaben, finanzierungen);
            final Finanzierung loaded = finanzierungRepository.findById(created.getId()).orElseThrow();
            assertThat(loaded.getArt()).isEqualTo(expectedArt);
        }
    }
}
